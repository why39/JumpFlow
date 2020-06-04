package com.hxy.provenance.neo4j;

public class NeoConstants {

    /**
     * 流程虚拟头节点
     */
    public static final String TYPE_CASE_HEAD = "CASE";

    /**
     * 流程环节节点类型
     */
    public static final String TYPE_TASK_NODE = "Task";

    /**
     * 用户节点
     */
    public static final String TYPE_USER_NODE = "User";

    /**
     * 前后关系
     */
    public static final String RELATION_NEXT = "next";

    /**
     * 相关关系
     */
    public static final String RELATION_RELATED = "related";

    /**
     * 最新节点ID
     */
    public static final String KEY_CASE_NEW_NODE_ID = "CaseNewNodeId";

    /**
     * 案件流程虚拟头部节点
     */
    public static final String KEY_CASE_HEAD_ID = "CaseHeadId";

    /**
     * 案件创建者节点ID
     */
    public static final String KEY_CASE_USER_ID = "CaseUserId";
}
