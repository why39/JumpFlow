package com.hxy.provenance.neo4j;

import com.hxy.provenance.neo4j.json.JSON;

import java.io.Serializable;
import java.util.List;

/**
 * 案件流程节点
 */
public class CaseDataNodeBean implements Serializable {

    /**
     * 所属案件id，对应CaseDataBean的_id
     */
    private String caseId;

    /**
     * 所属案件名称
     */
    private String caseName;

    /**
     * 案件详情
     */
    private String caseDetailUrl;

    /**
     * 节点名称
     */
    private String nodeName;

    /**
     * 所属流程结点id
     */
    private String taskId;


    /**
     * 节点涉及文书
     */
    private List<NodeFileBean> nodeFiles;

    /**
     * 节点创建时间
     */
    private String nodeCreateTime;

    /**
     * 节点创建用户
     */
    private NodeUserBean nodeCreateUser;

    /**
     * 备注
     */
    private String remark;

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getNodeCreateTime() {
        return nodeCreateTime;
    }

    public void setNodeCreateTime(String nodeCreateTime) {
        this.nodeCreateTime = nodeCreateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public List<NodeFileBean> getNodeFiles() {
        return nodeFiles;
    }

    public void setNodeFiles(List<NodeFileBean> nodeFiles) {
        this.nodeFiles = nodeFiles;
    }

    public NodeUserBean getNodeCreateUser() {
        return nodeCreateUser;
    }

    public void setNodeCreateUser(NodeUserBean nodeCreateUser) {
        this.nodeCreateUser = nodeCreateUser;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getNodeCreateUserJson(){
        if (nodeCreateUser != null) {
            try {
                return JSON.marshal(nodeCreateUser);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getNodeFilesJsonArray() {
        if (nodeFiles != null && nodeFiles.size() > 0) {
            String jsonArray = null;
            try {
                jsonArray = JSON.marshal(nodeFiles);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return jsonArray;
        }

        return null;
    }

    public String getCaseDetailUrl() {
        return caseDetailUrl;
    }

    public void setCaseDetailUrl(String caseDetailUrl) {
        this.caseDetailUrl = caseDetailUrl;
    }

    public static class NodeUserBean implements Serializable {
        /**
         * 用户id
         */
        private String userId;

        /**
         * 用户名称
         */
        private String userName;

        /**
         * 用户主页
         */
        private String userHomepage;

        public NodeUserBean(String userId, String userName) {
            this.userId = userId;
            this.userName = userName;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserHomepage() {
            return userHomepage;
        }

        public void setUserHomepage(String userHomepage) {
            this.userHomepage = userHomepage;
        }
    }

    public static class NodeFileBean implements Serializable {

        /**
         * 文件名
         */
        private String fileName;

        /**
         * 文件链接
         */
        private String fileUrl;

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getFileUrl() {
            return fileUrl;
        }

        public void setFileUrl(String fileUrl) {
            this.fileUrl = fileUrl;
        }
    }
}
