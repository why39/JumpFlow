package com.hxy.provenance.neo4j;

import com.hxy.provenance.neo4j.json.JSON;
import com.hxy.provenance.neo4j.json.JSONObject;
import org.neo4j.driver.v1.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.hxy.provenance.neo4j.NeoConstants.*;
import static org.neo4j.driver.v1.Values.parameters;

@Component
public class Neo4jFinalUtil {
    public static String NEO_SERVER_URL ;//= "bolt://127.0.0.1:7687";

    public static String NEO_SERVER_USER ;//= "neo4j";

    public static String NEO_SERVER_PSW ;//= "1992";

    private static Driver driver = null;

    static Logger logger = LoggerFactory.getLogger(Neo4jUtil.class);

    @Autowired
    public CaseDataMapper caseDataMapper;

    public static Neo4jFinalUtil finalUtil;

    @Autowired
    private Environment env;

    @PostConstruct
    public void init() {
        finalUtil = this;
        finalUtil.caseDataMapper = this.caseDataMapper;
        System.out.println("init >>>>>>>>>>>>>>>>> finalUtil : " + finalUtil);
        System.out.println("init >>>>>>>>>>>>>>>>> finalUtil.caseDataMapper : " + finalUtil.caseDataMapper);

    }

    private static Driver createDrive() {
        if (driver == null) {
            synchronized (Neo4jUtil.class) {
                if (driver == null) {
                    NEO_SERVER_URL = "bolt://"+finalUtil.env.getProperty("spring.data.neo4j.uri")+"/7687";
                    NEO_SERVER_USER = finalUtil.env.getProperty("spring.data.neo4j.username");
                    NEO_SERVER_PSW = finalUtil.env.getProperty("spring.data.neo4j.password");

                    driver = GraphDatabase.driver(NEO_SERVER_URL, AuthTokens.basic(NEO_SERVER_USER, NEO_SERVER_PSW));
                    return driver;
                }
            }
        }
        return driver;
    }

    public static Code test() {
        Code restfulResult = new Code();

        try {
            Driver driver = createDrive();
            Session session = driver.session();

            session.run("CREATE (a:Person {name: {name}, title: {title}})",
                    parameters("name", "Arthur001", "title", "King001"));

            StatementResult result = session.run("MATCH (a:Person) WHERE a.name = {name} " +
                            "RETURN a.name AS name, a.title AS title",
                    parameters("name", "Arthur001"));

            while (result.hasNext()) {
                Record record = result.next();
                System.out.println(record.get("title").asString() + " " + record.get("name").asString() + " " + record.get("id").asString());
            }

            session.close();
            driver.close();

        } catch (Exception e) {
            restfulResult.setResult(Constants.RESULT_STATE_ERROR);
            restfulResult.setMessage(e.getMessage());
        }

        logger.debug("restfulResult.getResult() : " + restfulResult.getResult());
        return restfulResult;
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
            if (map.containsKey("lastNodeId")) {
                lastNodeId = (String) map.get("lastNodeId");
            }
        }

        Code code = new Code();
        code.setLabel(label);

        Driver driver = createDrive();
        Session session = driver.session();

        String curTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        //当前时间作为nodeId
        map.put("createTime", curTime);
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


    public static String createUser(String uName, String uId, String caseId, String caseNodeId) {

        String label = TYPE_USER_NODE;
        Code code = new Code();
        code.setLabel(label);

        Driver driver = createDrive();
        Session session = driver.session();

        String curTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String userNodeId = null;
        try {

            // 查找用户，存在则直接创建关系
            StatementResult findResult = session.run("MATCH (u:" + label + ") where u.user_id = '" + uId + "' return u");

            if (findResult != null && findResult.hasNext()) {
                while (findResult.hasNext()) {
                    Record record = findResult.next();
                    userNodeId = record.fields().get(0).value().toString().replace("node<", "").replace(">", "");
                }
            } else {
                // 创建用户节点
                StatementResult createResult = session.run("CREATE (a:" + label + " {" +
                                "name: {name}" +
                                ", label: {label}" +
                                ", user_id: {user_id}" +
                                "}) return a",
                        parameters(
                                "name", uName
                                , "label", label
                                , "user_id", uId
                        ));

                while (createResult.hasNext()) {
                    Record record = createResult.next();
                    userNodeId = (record.fields().get(0).value().toString().replace("node<", "").replace(">", ""));
                }
            }

            if (userNodeId != null && !"".equals(userNodeId)) {
                Code relation = new Code();
                relation.setNodeFromId(userNodeId);
                relation.setNodeToId(caseNodeId);
                relation.setRelation("创建");//next
                relate(relation);
                code.setId(userNodeId);
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.debug(e.getMessage());
        }
        logger.debug(code.getId() + ">>>>>>");
        return code.getId();

    }

    /**
     * 返回生成节点的ID
     *
     * @param nodeBean 案件环节节点信息
     * @return
     */
    public static String createNode(CaseDataNodeBean nodeBean, String lastNodeId) {

        String label = TYPE_TASK_NODE;
        Code code = new Code();
        code.setLabel(label);

        Driver driver = createDrive();
        Session session = driver.session();

        String curTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        try {

            // 创建流程节点
            StatementResult createResult = session.run("CREATE (a:" + label + " {" +
                            "name: {name}" +
                            ", label: {label}" +
                            ", lastNodeId: {lastNodeId}" +
                            ", nodeName:{nodeName}" +
                            ", caseId: {caseId}" +
                            ", caseName: {caseName}" +
                            ", caseDetailUrl: {caseDetailUrl}" +
                            ", createTime: {createTime}" +
                            ", createUser: {createUser}" +
                            ", files: {files}" +
                            ", remark: {remark}" +
                            "}) return a",
                    parameters(
                            "name", nodeBean.getNodeName()
                            , "label", label
                            , "lastNodeId", lastNodeId
                            , "nodeName", nodeBean.getNodeName()
                            , "caseId", nodeBean.getCaseId()
                            , "caseName", nodeBean.getCaseName()
                            , "caseDetailUrl", nodeBean.getCaseDetailUrl()
                            , "createTime", curTime
                            , "createUser", nodeBean.getNodeCreateUserJson()
                            , "files", nodeBean.getNodeFilesJsonArray()
                            , "remark", nodeBean.getRemark()
                    ));

            while (createResult.hasNext()) {
                Record record = createResult.next();
                code.setId(record.fields().get(0).value().toString().replace("node<", "").replace(">", ""));
            }

            /**
             * 存在前置节点，则添加关系
             */
            if (lastNodeId != null && !"".equals(lastNodeId)) {

                Code relation = new Code();
                relation.setNodeFromId(lastNodeId);
                relation.setNodeToId(code.getId());
                relation.setRelation(NeoConstants.RELATION_NEXT);//next
                relate(relation);

            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug(e.getMessage());
        }
        logger.debug(code.getId() + ">>>>>>");
        return code.getId();
    }

    public static Code save(Code code) {

        try {
            Driver driver = createDrive();
            Session session = driver.session();

            StatementResult result = session.run("CREATE (a:" + code.getLabel() + " {" + code.getProperty() + "}) return a");

            while (result.hasNext()) {
                Record record = result.next();
                code.setId(record.fields().get(0).value().toString().replace("node<", "").replace(">", ""));
            }

//            session.close();
//            driver.close();

        } catch (Exception e) {
            code.setMessage(e.getMessage());
        }

        logger.debug("restfulResult.getResult() : " + code.getResult());
        return code;
    }

    public static Code update(Code code) {

        try {
            Driver driver = createDrive();
            Session session = driver.session();

            StatementResult result = session.run("MATCH (a:" + code.getLabel() + ") WHERE a." + code.getWhere() + " SET a." + code.getUpdate() + " return COUNT(a)");

            while (result.hasNext()) {
                Record record = result.next();
                code.setId(record.fields().get(0).value().toString().replace("node<", "").replace(">", ""));
            }

//            session.close();
//            driver.close();

        } catch (Exception e) {
            code.setMessage(e.getMessage());
        }

        logger.debug("restfulResult.getResult() : " + code.getResult());
        return code;
    }

    public static Code delete(Code code) {

        try {
            Driver driver = createDrive();
            Session session = driver.session();
            session.run("match (n) where ID(n) = " + code.getId() + " detach delete n");

//            session.close();
//            driver.close();

        } catch (Exception e) {
            code.setMessage(e.getMessage());
        }

        logger.debug("restfulResult.getResult() : " + code.getResult());
        return code;
    }

    /**
     * 不好用，直接用execute 吧
     *
     * @param code
     * @return
     */
    public static Code search(Code code) {

        try {
            Driver driver = createDrive();
            Session session = driver.session();

            StatementResult result = session.run("MATCH " + code.getProperty() +
                    " MATCH " + code.getRelation() +
                    " WHERE " + code.getWhere() +
                    " RETURN " + code.getResult());
            List<String> resultList = new ArrayList<String>();
            while (result.hasNext()) {
                Record record = result.next();
                resultList.add(record.get("id").toString() + " " + record.get("name").toString());
            }

//            session.close();
//            driver.close();

            code.setList(resultList);

        } catch (Exception e) {
            code.setMessage(e.getMessage());
        }

        logger.debug("restfulResult.getResult() : " + code.getResult());
        return code;
    }

    public Code execute(Code code) {

        try {
            Driver driver = createDrive();
            Session session = driver.session();

            StatementResult result = session.run(code.getContent());

            List<String> resultList = new ArrayList<String>();
            while (result.hasNext()) {
                Record record = result.next();
                resultList.add(record.fields().get(0).value().toString().replace("node<", "").replace(">", ""));
            }
            code.setList(resultList);

//            session.close();
//            driver.close();

        } catch (Exception e) {
            code.setMessage(e.getMessage());
        }

        logger.debug("restfulResult.getResult() : " + code.getResult());
        return code;
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
     * 溯源
     *
     * @param name 节点名称
     */
    public static void trace(String name) {
        Driver driver = createDrive();
        Session session = driver.session();
        session.run("MATCH (n:Task) WHERE n.name=\"" + name + "\" WITH n MATCH p = (n) - [*] -> (m:Task) RETURN m");
    }

    /**
     * 推理
     *
     * @param name
     */
    public static void inference(String name) {
        Driver driver = createDrive();
        Session session = driver.session();
        session.run("MATCH (n:Task) WHERE n.name=\"" + name + "\" WITH n MATCH p = (m:Task) - [*] ->(n) RETURN m");
    }


    public static int addCase(CaseDataBean dataBean) {
        //同时添加一个neo4j的虚拟头节点，用于存储整个案件的信息，并且所有属性世系数据都从这个案件开始
        String curTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String caseId = curTime;
        Map<String, Object> params = new HashMap<>();
        if (dataBean.getCase_id() != null && !"".equals(dataBean.getCase_id())) {
            caseId = dataBean.getCase_id();
        }

        String createUserName = dataBean.getUser_name();
        String createUserId = dataBean.getUser_id();

        params.put("name", dataBean.getCase_name());
        params.put("case_name", dataBean.getCase_name());
        params.put("case_category", dataBean.getCase_category());
        params.put("create_user_name", createUserName);
        params.put("user_id", createUserId);

        String headId = Neo4jFinalUtil.createKeyValues(caseId, NeoConstants.TYPE_CASE_HEAD, "", "开始", false, params);


        String userNodeId = Neo4jFinalUtil.createUser(createUserName, createUserId, caseId, headId);

        CaseProvDataBean provDataBean = new CaseProvDataBean();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(KEY_CASE_HEAD_ID, headId);
        jsonObject.put(KEY_CASE_NEW_NODE_ID, headId);
        jsonObject.put(KEY_CASE_USER_ID, userNodeId);
        provDataBean.setCase_id(caseId);
        provDataBean.setCase_prov_data(jsonObject.toString());

        finalUtil.caseDataMapper.updateCaseProvData(provDataBean);
        return finalUtil.caseDataMapper.addCase(dataBean);
    }

    public static String addCaseNode(CaseDataNodeBean caseDataNodeBean) {
        String caseId = caseDataNodeBean.getCaseId();
        JSONObject caseProvData = queryCaseProvData(caseId);
        if (caseProvData != null) {
            String lastNodeId = caseProvData.getStr(KEY_CASE_NEW_NODE_ID);
            String nodeId = Neo4jFinalUtil.createNode(caseDataNodeBean, lastNodeId);
            caseProvData.put(KEY_CASE_NEW_NODE_ID, nodeId);
            updateCaseProvData(caseDataNodeBean.getCaseId(), caseProvData);
            return nodeId;
        } else {
            return "-1";
        }
    }

    public static void updateCaseProvData(String case_id, String case_prov_data) {
        finalUtil.caseDataMapper.updateCaseProvData(new CaseProvDataBean(case_id, case_prov_data));
    }

    public static void updateCaseProvData(String case_id, JSONObject data) {
        updateCaseProvData(case_id, data.toString());
    }

    public static JSONObject queryCaseProvData(String case_id) {
        String data = finalUtil.caseDataMapper.queryCaseProvData(case_id);
        if (data != null && !"".equals(data)) {
            JSONObject jsonObject = null;
            try {
                jsonObject = JSON.unmarshal(data, JSONObject.class);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return jsonObject;
        }
        return null;
    }

    public static String addKVs(String caseId, String label, String relation, boolean reverse, Map<String, Object> map) {
        JSONObject caseProvData = queryCaseProvData(caseId);
        if (caseProvData != null) {
            String lastNodeId = null;
            if (map != null && map.get("lastNodeId") != null) {
                lastNodeId = (String) map.get("lastNodeId");
            } else {
                lastNodeId = caseProvData.getStr(label);//如果有相同标签的最新节点id则作为上一个节点id
            }

            if (lastNodeId == null || "".equals(lastNodeId)) {
                lastNodeId = caseProvData.getStr(KEY_CASE_HEAD_ID);//否则以案件虚拟头部节点作为前一个节点
                map.put("lastNodeId", lastNodeId);
            }

            String nodeId = Neo4jFinalUtil.createKeyValues(caseId, label, lastNodeId, relation, reverse, map);
            caseProvData.put(label, nodeId);
            updateCaseProvData(caseId, caseProvData);
            return nodeId;
        } else {
            return "-1";
        }
    }

    public final class Constants {

        // 返回状态：error
        public static final String RESULT_STATE_ERROR = "Error";
        // 返回状态：success
        public static final String RESULT_STATE_SUCCESS = "Success";
        // 父级菜单最上层
        public static final String PARENT_MAX = "0";

        // 短信供应商：阿里大鱼
        public static final String SUPPLIER_ALI = "1";

        // 验证码是否被使用 : 否
        public static final String VCODE_USED_NO = "0";
        // 验证码是否被使用: 是
        public static final String VCODE_USED_YES = "1";

    }

}
