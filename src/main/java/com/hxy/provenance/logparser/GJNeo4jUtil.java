package com.hxy.provenance.logparser;

import com.hxy.provenance.neo4j.*;
import com.hxy.provenance.neo4j.json.JSON;
import com.hxy.provenance.neo4j.json.JSONObject;
import org.neo4j.driver.v1.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.hxy.provenance.neo4j.NeoConstants.*;
import static org.neo4j.driver.v1.Values.parameters;

@Component
public class GJNeo4jUtil {
    public static String NEO_SERVER_URL;//= "bolt://127.0.0.1:7687";

    public static String NEO_SERVER_USER;//= "neo4j";

    public static String NEO_SERVER_PSW;//= "1992";

    private static Driver driver = null;

    static Logger logger = LoggerFactory.getLogger(GJNeo4jUtil.class);

    public static GJNeo4jUtil finalUtil;

    @Autowired
    private Environment env;

    @PostConstruct
    public void init() {
        finalUtil = this;
        System.out.println("init >>>>>>>>>>>>>>>>> finalUtil : " + finalUtil);

    }

    private static Driver createDrive() {
        if (driver == null) {
            synchronized (GJNeo4jUtil.class) {
                if (driver == null) {
                    NEO_SERVER_URL = "bolt://" + finalUtil.env.getProperty("spring.data.neo4j.uri") + "/7687";
                    NEO_SERVER_USER = finalUtil.env.getProperty("spring.data.neo4j.username");
                    NEO_SERVER_PSW = finalUtil.env.getProperty("spring.data.neo4j.password");

                    driver = GraphDatabase.driver(NEO_SERVER_URL, AuthTokens.basic(NEO_SERVER_USER, NEO_SERVER_PSW));
                    return driver;
                }
            }
        }
        return driver;
    }


    /**
     * 插入自定义key value节点
     *
     * @param map
     * @return
     */
    public static String createKeyValues(String caseId, String label, String lastNodeId, String relationName, boolean reverse, Map<String, Object> map) {
        if (label == null || "".equals(label)) {
            if (!map.containsKey("label")) {
                return null;
            } else {
                label = (String) map.get("label");
            }
        } else {
            map.put("label", label);
        }

        map.put("caseId", caseId);

        if (lastNodeId == null || "".equals(lastNodeId)) {
            if (map.containsKey(NeoConstants.KEY_LAST_NODE_ID)) {
                lastNodeId = (String) map.get(NeoConstants.KEY_LAST_NODE_ID);
            }
        }

        Code code = new Code();
        code.setLabel(label);

        Driver driver = createDrive();
        Session session = driver.session();

        String curTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        //当前时间作为nodeId
        map.put("timestamp", curTime);
        try {

            StringBuilder terminal = new StringBuilder("CREATE (a:");
            terminal.append(label);
            terminal.append(" {");
            Set<String> keys = map.keySet();
            for (String k : keys) {
                terminal.append(k);
                terminal.append(":");
                terminal.append("{" + k + "},");
            }
            terminal.deleteCharAt(terminal.length() - 1);
            terminal.append("}) return a");


            Object[] params = new Object[keys.size() * 2];
            int index = 0;
            for (String k : keys) {
                params[index] = k;
                params[index + 1] = map.get(k);
                index += 2;
            }

            // 创建流程节点
            StatementResult createResult = session.run(terminal.toString(), map);

            while (createResult.hasNext()) {
                Record record = createResult.next();
                code.setId(record.fields().get(0).value().toString().replace("node<", "").replace(">", ""));
            }

            /**
             * 存在前置节点，则添加关系
             */
            if (lastNodeId != null && lastNodeId != "") {
                if (relationName == null) {
                    relationName = NeoConstants.RELATION_RELATED;
                }
                Code relation = new Code();
                if (reverse) {
                    relation.setNodeToId(lastNodeId);
                    relation.setNodeFromId(code.getId());
                } else {
                    relation.setNodeFromId(lastNodeId);
                    relation.setNodeToId(code.getId());
                }
                relation.setRelation(relationName);//related
                relate(relation);

            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug(e.getMessage());
        }
        logger.debug(code.getId() + ">>>>>>");
        return code.getId();
    }


    public static Code relate(Code code) {

        try {
            Driver driver = createDrive();
            Session session = driver.session();

            session.run("MATCH (a), (b)" +
                    "WHERE ID(a) = " + code.getNodeFromId() + " AND ID(b) = " + code.getNodeToId()
                    + " CREATE (a)-[:" + code.getRelation() + "]->(b)");

//            session.close();
//            driver.close();

            logger.debug("relate>>>" + code.getNodeFromId() + " - " + code.getNodeToId());
        } catch (Exception e) {
            code.setMessage(e.getMessage());
        }


        return code;
    }


    /**
     * 流程环节
     *
     * @param BMSAH
     * @param label
     * @param relation
     * @param reverse
     * @param map
     * @return
     */
    public static String addTaskNode(String BMSAH, String label, String relation, boolean reverse, Map<String, Object> map) {
        //先查找上一个环节节点，然后添加到他的末尾
        String lastNodeId = null;
        Driver driver = createDrive();
        Session session = driver.session();
        StatementResult findResult = session.run("MATCH (c:CASE) where c.caseId = '" + BMSAH + "' with c MATCH p = (c) - [*] -> (m:" + label + ") return m");


        if (findResult != null && findResult.hasNext()) {
            while (findResult.hasNext()) {
                Record record = findResult.next();
                lastNodeId = record.fields().get(0).value().toString().replace("node<", "").replace(">", ""); //lastNode肯定有，因为有一个CASE Node.
                logger.debug("已有该属性节点》》》》" + lastNodeId);
            }
        }

        String nodeId = GJNeo4jUtil.createKeyValues(BMSAH, label, lastNodeId, relation, reverse, map);
        return nodeId;
    }

    /**
     * 单个的操作节点
     *
     * @param BMSAH
     * @param label
     * @param relation
     * @param reverse
     * @param map
     * @return
     */
    public static String addActionNode(String BMSAH, String label, String relation, boolean reverse, Map<String, Object> map) {
        String lastNodeId = null;
        if (map != null && map.get(NeoConstants.KEY_LAST_NODE_ID) != null) {
            lastNodeId = (String) map.get(NeoConstants.KEY_LAST_NODE_ID);
        }

        String nodeId = GJNeo4jUtil.createKeyValues(BMSAH, label, lastNodeId, relation, reverse, map);
        return nodeId;
    }

    public static String addPropertyNode(String BMSAH, String label, String relation, boolean reverse, Map<String, Object> map) {
        String lastNodeId = null;
        String lastNodeValue = "";

        //先查询有没有这个BMSAH的属性节点，没有就插入，有就返回该属性的最后一个节点id，并作为当前插入的lastNode
        Driver driver = createDrive();
        Session session = driver.session();
        StatementResult findResult = session.run("MATCH (c:CASE) where c.caseId = '" + BMSAH + "' with c MATCH p = (c) - [*] -> (m:" + label + ") return m");


        if (findResult != null && findResult.hasNext()) {
            while (findResult.hasNext()) {
                Record record = findResult.next();
                lastNodeId = record.fields().get(0).value().toString().replace("node<", "").replace(">", ""); //lastNode肯定有，因为有一个CASE Node.
                logger.debug("已有该属性节点》》》》" + lastNodeId);
                String lastNodeLable = record.fields().get(0).value().get(label).toString(); //取出来的值前后加了两个双引号
                lastNodeValue = lastNodeLable.substring(1, lastNodeLable.length() - 1);
            }
        }


        //附加流程信息
        StatementResult findTaskResult = session.run("MATCH (c:CASE) where c.caseId = '" + BMSAH + "' with c MATCH p = (c) - [*] -> (m:Task) return m");
        if (findTaskResult != null && findTaskResult.hasNext()) {
            while (findTaskResult.hasNext()) {
                Record record = findTaskResult.next();
                String lastTaskLable = record.fields().get(0).value().get("name").toString(); //取出环节名称
                String lastTaskName= lastTaskLable.substring(1, lastTaskLable.length() - 1);
                map.put("所属环节", lastTaskName);
            }
        }

        if (!map.get(label).equals(lastNodeValue)) {
            String nodeId = GJNeo4jUtil.createKeyValues(BMSAH, label, lastNodeId, relation, reverse, map);
            return nodeId;
        }

        return lastNodeId;

    }

    public static String addCase(GJAJEntity dataBean) {
        //同时添加一个neo4j的虚拟头节点，用于存储整个案件的信息，并且所有属性世系数据都从这个案件开始

        //先查询有没有这个BMSAH的case，没有就插入，有就返回节点id
        Driver driver = createDrive();
        Session session = driver.session();
        StatementResult findResult = session.run("MATCH (c:CASE) where c.caseId = '" + dataBean.getBMSAH() + "' return c");

        String caseNodeId;
        if (findResult != null && findResult.hasNext()) {
            while (findResult.hasNext()) {
                Record record = findResult.next();
                caseNodeId = record.fields().get(0).value().toString().replace("node<", "").replace(">", "");
                logger.debug("已有案件》》》》" + caseNodeId);
                return caseNodeId;
            }
        }

        String caseId = dataBean.getBMSAH();

        String createUserName = dataBean.getCBDW_MC();
        Map<String, Object> params = new HashMap<>();

        params.put("name", dataBean.getAJLB_MC());
        params.put("案件名", dataBean.getAJMC());
        params.put("操作人", createUserName);

        String headId = GJNeo4jUtil.createKeyValues(caseId, NeoConstants.TYPE_CASE_HEAD, "", "开始", false, params);

        return headId;
    }


}
