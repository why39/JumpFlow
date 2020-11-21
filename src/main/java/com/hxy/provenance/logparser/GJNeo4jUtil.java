package com.hxy.provenance.logparser;

import com.hxy.modules.common.utils.StringUtils;
import com.hxy.provenance.neo4j.*;
import org.neo4j.driver.v1.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class GJNeo4jUtil {
    public static String NEO_SERVER_URL;//= "bolt://127.0.0.1:7687";

    public static String NEO_SERVER_USER;//= "neo4j";

    public static String NEO_SERVER_PSW;//= "1992";
    public static GJNeo4jUtil finalUtil;
    static Logger logger = LoggerFactory.getLogger(GJNeo4jUtil.class);
    static SimpleDateFormat format1 = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒SSS");
    static SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    private static Driver driver = null;
    @Autowired
    private Environment env;

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
    public static String createKeyValues(String label, String lastNodeId, String relationName, boolean reverse, Map<String, Object> map) {
        if (map == null) {
            map = new HashMap<String, Object>();
        }
        if (label == null || "".equals(label)) {
            if (!map.containsKey("label")) {
                return null;
            } else {
                label = (String) map.get("label");
            }
        } else {
            map.put("label", label);
        }

        if (StringUtils.isEmpty(lastNodeId)) {
            if (map.containsKey(NeoConstants.KEY_LAST_NODE_ID)) {
                lastNodeId = (String) map.get(NeoConstants.KEY_LAST_NODE_ID);
            }
        } else {
            map.put(NeoConstants.KEY_LAST_NODE_ID, lastNodeId);
        }

        Code code = new Code();
        code.setLabel(label);

        Driver driver = createDrive();
        Session session = driver.session();

        String curTime = format1.format(new Date()); //必须精确到毫秒，方便后面查询的时候逆序排列找到最后一个节点
        //当前时间作为nodeId

        map.put("创建时间", curTime);
        map.put("timestamp", format2.format(new Date()));
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

        map.put("caseId", BMSAH);
        String nodeId = GJNeo4jUtil.createKeyValues(label, lastNodeId, relation, reverse, map);
        return nodeId;
    }

    /**
     * 添加用户节点
     *
     * @param RM
     * @param relatedNodeId
     * @param relation
     * @return
     */
    public static String addUserNode(String RM, String relatedNodeId, String relation) {
        if (StringUtils.isEmpty(RM) || "null".equals(RM)) {
            return null;
        }
        //如果有该人名节点，则直接和relatedNodeId关联，否则先创建，再关联。
        String userNodeId = null;
        Driver driver = createDrive();
        Session session = driver.session();
        StatementResult findResult = session.run("MATCH (c:USER) where c.name = '" + RM + "' return c");


        if (findResult != null && findResult.hasNext()) {
            while (findResult.hasNext()) {
                Record record = findResult.next();
                userNodeId = record.fields().get(0).value().toString().replace("node<", "").replace(">", ""); //lastNode肯定有，因为有一个CASE Node.
                //关联
                Code code = new Code();
                code.setNodeFromId(userNodeId);
                code.setNodeToId(relatedNodeId);
                code.setRelation(relation);
                code.setLabel(relation);
                relate(code);
            }
        }

        if (StringUtils.isEmpty(userNodeId)) {
            Map<String, Object> par = new HashMap<>();
            par.put("name", RM);
            userNodeId = createKeyValues("USER", relatedNodeId, relation, true, par);
        }
        return userNodeId;

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

        map.put("caseId", BMSAH);
        String nodeId = GJNeo4jUtil.createKeyValues(label, lastNodeId, relation, reverse, map);
        return nodeId;
    }

    public static String addPropertyNode(String BMSAH, String label, String relation, boolean reverse, Map<String, Object> map) {
        String lastNodeId = null;
        String lastTaskNodeId = (String) map.get("taskNodeId");
        String lastTaskNodeName = (String) map.get("taskNodeName");
        String lastNodeValue = null;

        //先查询有没有这个BMSAH的属性节点，没有就插入，有就返回该属性的最后一个节点id，并作为当前插入的lastNode
        Driver driver = createDrive();
        Session session = driver.session();
        //要取最后一个创建的节点，不然会出现多个节点连接同一个节点上的情况
        StatementResult findResult = session.run("MATCH (c:CASE) where c.caseId = '" + BMSAH + "' with c MATCH p = (c) - [*] -> (m:" + label + ") return m Order by m.创建时间 desc Limit 1");


        if (findResult != null && findResult.hasNext()) {
            while (findResult.hasNext()) {
                Record record = findResult.next();
                lastNodeId = record.fields().get(0).value().toString().replace("node<", "").replace(">", ""); //lastNode肯定有，因为有一个CASE Node.
                if (!lastNodeId.equals(map.get("CaseNodeId"))) {
                    //如果上一个节点不是CASE节点，则为当前节点添加KEY_LAST_NODE_ID属性，并且获取上一个节点的值作为后续比较用
                    map.put(NeoConstants.KEY_LAST_NODE_ID, lastNodeId);
                    String lastNodeLable = record.fields().get(0).value().get(label).toString(); //取出来的值前后加了两个双引号
                    lastNodeValue = lastNodeLable.substring(1, lastNodeLable.length() - 1);
                }
                logger.debug("已有该属性节点》》》》" + lastNodeId);

            }
        }

        if (StringUtils.isEmpty(lastNodeId)) {
            lastNodeId = (String) map.get("CaseNodeId");
            map.put(NeoConstants.KEY_LAST_NODE_ID, lastNodeId);
        }


        if (!StringUtils.isEmpty(lastTaskNodeId)) {
            map.put("所属环节", lastTaskNodeName);
            map.put(NeoConstants.KEY_LAST_NODE_ID, lastTaskNodeId);
        } else {
            //附加流程信息
            StatementResult findTaskResult = session.run("MATCH (c:CASE) where c.caseId = '" + BMSAH + "' with c MATCH p = (c) - [*] -> (m:Task) return m Order by m.创建时间 desc Limit 1");
            if (findTaskResult != null && findTaskResult.hasNext()) {
                while (findTaskResult.hasNext()) {
                    Record record = findTaskResult.next();

                    String lastTaskLable = record.fields().get(0).value().get("name").toString(); //取出环节名称
                    String lastTaskName = lastTaskLable.substring(1, lastTaskLable.length() - 1);
                    map.put("所属环节", lastTaskName);
                }
            }
        }

        if (lastNodeValue == null || map.get(label) == null || !map.get(label).equals(lastNodeValue)) {
            map.put("caseId", BMSAH);

            String nodeId = GJNeo4jUtil.createKeyValues(label, lastNodeId, relation, reverse, map);

            Code code = new Code();
            code.setNodeFromId(lastTaskNodeId);
            code.setNodeToId(nodeId);
            code.setLabel("相关");
            code.setRelation("相关");
            relate(code);

            return nodeId;
        }

        return lastNodeId;

    }

    public static String queryCase(GJAJEntity dataBean) {
        //先查询有没有这个BMSAH的case，没有就插入，有就返回节点id
        Driver driver = createDrive();
        Session session = driver.session();
        StatementResult findResult = session.run("MATCH (c:CASE) where c.caseId = '" + dataBean.getBMSAH() + "' return c");

        String caseNodeId = null;
        if (findResult != null && findResult.hasNext()) {
            while (findResult.hasNext()) {
                Record record = findResult.next();
                caseNodeId = record.fields().get(0).value().toString().replace("node<", "").replace(">", "");

            }
        }
        return caseNodeId;
    }

    public static String addCase(GJAJEntity dataBean) {
        //同时添加一个neo4j的虚拟头节点，用于存储整个案件的信息，并且所有属性世系数据都从这个案件开始
        Driver driver = createDrive();
        Session session = driver.session();
        String createUserName = dataBean.getCBDW_MC();
        Map<String, Object> params = new HashMap<>();

        params.put("name", dataBean.getAJMC());
        params.put("案件名", dataBean.getAJMC());
        params.put("承办单位", createUserName);
        params.put("案件类别", dataBean.getAJLB_MC());
        params.put("caseId", dataBean.getBMSAH());
        String caseNodeId = GJNeo4jUtil.createKeyValues(NeoConstants.TYPE_CASE_HEAD, "", "", false, params);

        //再查询有没有相同承办单位节点，有就直接关联，没有就创建并关联。
        StatementResult cbdwFindResult = session.run("MATCH (c:承办单位) where c.CBDW_MC = '" + dataBean.getCBDW_MC() + "' return c");

        String cbdwNodeId = null;
        if (cbdwFindResult != null && cbdwFindResult.hasNext()) {
            while (cbdwFindResult.hasNext()) {
                Record record = cbdwFindResult.next();
                cbdwNodeId = record.fields().get(0).value().toString().replace("node<", "").replace(">", "");

                Code code = new Code();
                code.setNodeFromId(cbdwNodeId);
                code.setNodeToId(caseNodeId);
                code.setLabel("承办");
                code.setRelation("承办");
                relate(code);
                return caseNodeId;
            }
        }


        if (StringUtils.isEmpty(cbdwNodeId)) {
            Map<String, Object> cbdw = new HashMap<>();
            cbdw.put("name", "承办单位");
            cbdwNodeId = GJNeo4jUtil.createKeyValues("承办单位", caseNodeId, "承办", true, cbdw);
        }

        return caseNodeId;
    }

    @PostConstruct
    public void init() {
        finalUtil = this;
    }


}
