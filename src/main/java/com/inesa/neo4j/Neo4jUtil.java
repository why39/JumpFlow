package com.inesa.neo4j;

import com.hxy.modules.activiti.dto.ProcessTaskDto;
import com.hxy.modules.common.utils.RedisUtil;
import com.hxy.modules.common.utils.SpringContextUtils;
import com.hxy.modules.common.utils.UserUtils;
import com.inesa.common.utils.Constants;
import com.inesa.neo4j.entity.Code;
import org.apache.http.util.TextUtils;
import org.neo4j.driver.v1.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.neo4j.driver.v1.Values.parameters;

public class Neo4jUtil {

    private static RedisUtil redisUtils = (RedisUtil) SpringContextUtils.getBean("redisUtil");

    private static Driver driver = null;

    static Logger logger = LoggerFactory.getLogger(Neo4jUtil.class);

    private static Driver createDrive() {
        if (driver == null) {
            synchronized (Neo4jUtil.class) {
                if (driver == null) {
                    driver = GraphDatabase.driver("bolt://127.0.0.1:7687", AuthTokens.basic("neo4j", "1992"));
                    return driver;
                }
            }
        }
        return driver;
    }

    public static List<Record> test() {
        Code restfulResult = new Code();
        List<Record> records = null;
        try {
            Driver driver = createDrive();
            Session session = driver.session();

            StatementResult result = session.run("match (n:Task) where n.instanceId=\"387501\" with n match p=(n)- [*] ->(m) return p");

            records = result.list();

            while (result.hasNext()) {
                Record record = result.next();
                System.out.println(record.get("name").asString());
            }

            session.close();
            driver.close();

        } catch (Exception e) {
            restfulResult.setResult(Constants.RESULT_STATE_ERROR);
            restfulResult.setMessage(e.getMessage());
        }

        logger.debug("restfulResult.getResult() : " + restfulResult.getResult());
        return records;
    }

    // 创建审批(Remark)数据节点
    public static Code createDataNode(ProcessTaskDto processTaskDto) {
        Code code = new Code();
        code.setLabel(processTaskDto.getTaskId());

        Driver driver = createDrive();
        Session session = driver.session();

        try {

            //如果上一个mark相同，则把当前节点taskid加入到上一个数据节点中。不存在mark相同的则创建新节点。
            if (!TextUtils.isEmpty(redisUtils.getString("lastRemark")) && redisUtils.getString("lastRemark").equals(processTaskDto.getRemark())) {
                //先找到上一个相同的Remark节点
                StatementResult result = session.run("MATCH (a:Remark) WHERE a.taskId = {taskId} RETURN a", parameters("taskId", redisUtils.getString("lastTaskId")));

                while (result.hasNext()) {
                    Record record = result.next();
                    String taskIds = record.fields().get(0).value().get("taskIds").asString();

                    String newTaskIds = (taskIds + "," + processTaskDto.getTaskId());

                    StatementResult updateResult = session.run("MATCH (a:Remark) WHERE a.taskId = {taskId} SET a.taskIds = {taskIds} RETURN a", parameters("taskId", redisUtils.getString("lastTaskId"), "taskIds", newTaskIds));

                    logger.info(updateResult.hasNext() + "");


                }
            } else {


                StatementResult createResult = session.run("CREATE (a:Remark {" +
                                "name: {name}" +
                                ", nodeName: {nodeName}" +
                                ", label: {label}" +
                                ", taskId: {taskId}" +
                                ", taskIds: {taskIds}" +
                                ", taskName:{taskName}" +
                                ", instanceId: {instanceId}" +
                                ", busId: {busId}" +
                                ", busName: {busName}" +
                                ", status: {status}" +
                                ", dealId: {dealId}" +
                                ", dealTime: {dealTime}" +
                                ", dealUserName: {dealUserName}" +
                                ", remark: {remark}" +
                                "})",
                        parameters(
                                "name", processTaskDto.getRemark()
                                , "nodeName", processTaskDto.getTaskName()
                                , "taskId", processTaskDto.getTaskId()
                                , "taskIds", processTaskDto.getTaskId()
                                , "label", "Remark"
                                , "taskName", processTaskDto.getTaskName()
                                , "instanceId", (processTaskDto.getInstanceId() + "_remark")
                                , "busId", processTaskDto.getBusId()
                                , "busName", processTaskDto.getBusName()
                                , "status", processTaskDto.getStatus()
                                , "dealId", processTaskDto.getDealId()
                                , "dealTime", (new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date())
                                , "dealUserName", UserUtils.getCurrentUser().getLoginName()
                                , "remark", processTaskDto.getRemark()
                        ));

                StatementResult result = session.run("MATCH (a:Remark) WHERE a.taskId = {taskId} " +
                                "RETURN a",
                        parameters("taskId", processTaskDto.getTaskId()));

                while (result.hasNext()) {
                    Record record = result.next();
                    code.setId(record.fields().get(0).value().toString().replace("node<", "").replace(">", ""));
                    code.setLabel("Remark");
                }

                /**
                 * 如果已经存在相同instanceId的节点，且存在前置节点，则添加关系
                 */
                if (!TextUtils.isEmpty(redisUtils.getString("lastRemarkLabel")) && !TextUtils.isEmpty(redisUtils.getString((processTaskDto.getInstanceId() + "_remark")))) {

                    if ((processTaskDto.getInstanceId() + "_remark").equals(redisUtils.getString(processTaskDto.getInstanceId() + "_remark"))) {
                        Code relation = new Code();

                        relation.setNodeFromId(redisUtils.getString("lastRemarkId"));
                        relation.setNodeFromLabel(redisUtils.getString("lastRemarkLabel"));
                        relation.setNodeToId(code.getId());
                        relation.setNodeToLabel(code.getLabel());
                        relation.setRelation("next");
                        relate(relation);
                    }

                }
                redisUtils.setString("lastTaskId", processTaskDto.getTaskId());
                redisUtils.setString("lastRemarkId", code.getId());
                redisUtils.setString("lastRemarkLabel", code.getLabel());
            }
            redisUtils.setString("lastRemark", processTaskDto.getRemark());
            redisUtils.setString(processTaskDto.getInstanceId() + "_remark", processTaskDto.getInstanceId() + "_remark");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return code;
    }

    public static Code createNode(ProcessTaskDto processTaskDto) {

        Code code = new Code();
        code.setLabel(processTaskDto.getTaskId());

        Driver driver = createDrive();
        Session session = driver.session();

        String curTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        try {

            // 创建流程节点
            StatementResult createResult = session.run("CREATE (a:Task {" +
                            "name: {name}" +
                            ", label: {label}" +
                            ", taskId: {taskId}" +
                            ", taskName:{taskName}" +
                            ", fileName:{fileName}" +
                            ", formName:{formName}" +
                            ", instanceId: {instanceId}" +
                            ", busId: {busId}" +
                            ", busName: {busName}" +
                            ", status: {status}" +
                            ", dealId: {dealId}" +
                            ", dealTime: {dealTime}" +
                            ", dealUserName: {dealUserName}" +
                            ", remark: {remark}" +
                            "})",
                    parameters(
                            "name", processTaskDto.getTaskName()
                            , "taskId", processTaskDto.getTaskId()
                            , "label", "Task"
                            , "taskName", processTaskDto.getTaskName()
                            , "fileName", processTaskDto.getTaskName()+"文书材料"
                            , "formName", processTaskDto.getTaskName()+"案卡数据"
                            , "instanceId", processTaskDto.getInstanceId()
                            , "busId", processTaskDto.getBusId()
                            , "busName", processTaskDto.getBusName()
                            , "status", processTaskDto.getStatus()
                            , "dealId", processTaskDto.getDealId()
                            , "dealTime", curTime
                            , "dealUserName", UserUtils.getCurrentUser().getUserName()
                            , "remark", processTaskDto.getRemark()
                    ));

            StatementResult result = session.run("MATCH (a:Task) WHERE a.taskId = {taskId} " +
                            "RETURN a",
                    parameters("taskId", processTaskDto.getTaskId()));

            while (result.hasNext()) {
                Record record = result.next();
                code.setId(record.fields().get(0).value().toString().replace("node<", "").replace(">", ""));
                code.setLabel("Task");
            }

            /**
             * 如果已经存在相同instanceId的节点，且存在前置节点，则添加关系
             */
            if (!TextUtils.isEmpty(redisUtils.getString("lastCodeId")) && !TextUtils.isEmpty(redisUtils.getString(processTaskDto.getInstanceId()))) {

                if (processTaskDto.getInstanceId().equals(redisUtils.getString(processTaskDto.getInstanceId()))) {
                    Code relation = new Code();
                    relation.setNodeFromId(redisUtils.getString("lastCodeId"));
                    relation.setNodeFromLabel(redisUtils.getString("lastCodeLabel"));
                    relation.setNodeToId(code.getId());
                    relation.setNodeToLabel(code.getLabel());

                    String lastTime = redisUtils.getString("lastTime");
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String next = "next";
                    try {
                        Date d1 = df.parse(curTime);
                        Date d2 = df.parse(lastTime);
                        long diff = d1.getTime() - d2.getTime();//这样得到的差值是毫秒级别
                        long days = diff / (1000 * 60 * 60 * 24);

                        long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
                        long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
                        next = ("" + days + "天" + hours + "小时" + minutes + "分");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    relation.setRelation("next");//next
                    relate(relation);
                }

            }


            redisUtils.setString("lastCodeLabel", code.getLabel());
            redisUtils.setString("lastCodeId", code.getId());
            redisUtils.setString("lastTime", curTime);
            redisUtils.setString(processTaskDto.getInstanceId(), processTaskDto.getInstanceId());

//            createDataNode(processTaskDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return code;
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

            session.run("MATCH (a:" + code.getNodeFromLabel() + "), (b:" + code.getNodeToLabel() + ") " +
                    "WHERE ID(a) = " + code.getNodeFromId() + " AND ID(b) = " + code.getNodeToId()
                    + " CREATE (a)-[:" + code.getRelation() + "]->(b)");

//            session.close();
//            driver.close();

        } catch (Exception e) {
            code.setMessage(e.getMessage());
        }

        logger.debug("restfulResult.getResult() : " + code.getResult());
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
}
