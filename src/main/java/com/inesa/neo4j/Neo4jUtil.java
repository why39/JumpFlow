package com.inesa.neo4j;

import com.hxy.modules.activiti.dto.ProcessTaskDto;
import com.hxy.modules.common.utils.RedisUtil;
import com.hxy.modules.common.utils.SpringContextUtils;
import com.inesa.common.utils.Constants;
import com.inesa.neo4j.entity.Code;
import org.apache.http.util.TextUtils;
import org.neo4j.driver.v1.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
                    driver = GraphDatabase.driver("bolt://10.131.233.173:7687", AuthTokens.basic("neo4j", "1992"));
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

    public static Code createNode(ProcessTaskDto processTaskDto) {

        Code code = new Code();
        code.setLabel(processTaskDto.getTaskId());

        Driver driver = createDrive();
        Session session = driver.session();

        try {

            StatementResult createResult = session.run("CREATE (a:Task {" +
                            "name: {name}" +
                            ", label: {label}" +
                            ", taskId: {taskId}" +
                            ", taskName:{taskName}" +
                            ", instanceId: {instanceId}" +
                            ", busId: {busId}" +
                            ", busName: {busName}" +
                            ", status: {status}" +
                            ", dealId: {dealId}" +
                            ", dealTime: {dealTime}" +
                            ", startUserName: {startUserName}" +
                            ", nextUserName: {nextUserName}" +
                            ", nextUserId: {nextUserId}" +
                            "})",
                    parameters(
                            "name", processTaskDto.getTaskName()
                            ,"taskId", processTaskDto.getTaskId()
                            , "label", "Task"
                            , "taskName", processTaskDto.getTaskName()
                            , "instanceId", processTaskDto.getInstanceId()
                            , "busId", processTaskDto.getBusId()
                            , "busName", processTaskDto.getBusName()
                            , "status", processTaskDto.getStatus()
                            , "dealId", processTaskDto.getDealId()
                            , "dealTime", processTaskDto.getDealTime()
                            , "startUserName", processTaskDto.getStartUserName()
                            , "nextUserName", processTaskDto.getGetNextUserNames()
                            , "nextUserId", processTaskDto.getNextUserIds()
                    ));

            StatementResult result = session.run("MATCH (a:Task) WHERE a.taskId = {taskId} " +
                            "RETURN a",
                    parameters("taskId", processTaskDto.getTaskId()));

            while (result.hasNext()) {
                Record record = result.next();
                code.setId(record.fields().get(0).value().toString().replace("node<", "").replace(">", ""));
                code.setLabel("Task");
            }

//            session.close();
//            driver.close();

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
                    relation.setRelation("next");
                    relate(relation);
                }

            }


            redisUtils.setString("lastCodeLabel", code.getLabel());
            redisUtils.setString("lastCodeId", code.getId());
            redisUtils.setString(processTaskDto.getInstanceId(), processTaskDto.getInstanceId());

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
