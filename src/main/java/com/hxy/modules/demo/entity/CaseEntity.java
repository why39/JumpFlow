package com.hxy.modules.demo.entity;

import com.hxy.modules.activiti.annotation.ActField;
import com.hxy.modules.activiti.annotation.ActTable;
import com.hxy.modules.common.entity.ActivitiBaseEntity;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

/**
 * 类的功能描述.
 * 请假demo
 *
 * @Auther hxy
 * @Date 2017/7/27
 */
@ActTable(tableName = "caseaply", pkName = "id")
public class CaseEntity extends ActivitiBaseEntity {

    private String id;

    private String userId;

    @ActField(name = "《人民监督员监督案件受理登记表》")
    private String djb;//受理环节

    @ActField(name = "《补充移送材料通知书》")
    private String bccl;//审查环节

    @ActField(name = "《人民监督员监督案件审批表》")
    private String spb;//审查环节

    @ActField(name = "《移送函》")
    private String ysh;//移送环节

    @ActField(name = "《人民监督员监督案件通知书》")
    private String tzs;//组织评议环节

    @ActField(name = "《人民监督员表决意见通知书》")
    private String bjyj;//告知表决意见

    @ActField(name = "《人民监督员监督案件处理结果通知书》")
    private String cljg;//反馈处理结果环节

    private String fields;
    private List<String> filesUrl;//数据库不可能为每个文件名新建一列。一个List存文书的具体地址，还有个List用来存文书的名称
    private String title;

    /**
     * 案件详情
     */
    @NotEmpty(message = "案件详情说明")
    private String leavewhy;

    /**
     * 案件开始人员
     */
    private String leaveUser;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDjb() {
        return djb;
    }

    public void setDjb(String djb) {
        this.djb = djb;
    }

    public String getBccl() {
        return bccl;
    }

    public void setBccl(String bccl) {
        this.bccl = bccl;
    }

    public String getSpb() {
        return spb;
    }

    public void setSpb(String spb) {
        this.spb = spb;
    }

    public String getYsh() {
        return ysh;
    }

    public void setYsh(String ysh) {
        this.ysh = ysh;
    }

    public String getTzs() {
        return tzs;
    }

    public void setTzs(String tzs) {
        this.tzs = tzs;
    }

    public String getBjyj() {
        return bjyj;
    }

    public void setBjyj(String bjyj) {
        this.bjyj = bjyj;
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String files) {
        this.fields = files;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLeavewhy() {
        return leavewhy;
    }

    public void setLeavewhy(String leavewhy) {
        this.leavewhy = leavewhy;
    }

    public String getLeaveUser() {
        return leaveUser;
    }

    public void setLeaveUser(String leaveUser) {
        this.leaveUser = leaveUser;
    }

    public String getCljg() {
        return cljg;
    }

    public void setCljg(String cljg) {
        this.cljg = cljg;
    }
}
