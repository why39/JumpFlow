package com.hxy.modules.demo.entity;

import com.hxy.modules.activiti.annotation.ActField;
import com.hxy.modules.activiti.annotation.ActTable;
import com.hxy.modules.common.entity.ActivitiBaseEntity;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * 类的功能描述.
 * 请假demo
 * @Auther hxy
 * @Date 2017/7/27
 */
@ActTable(tableName = "caseaply",pkName="id")
public class CaseEntity extends ActivitiBaseEntity {

    private String id;

    private String userId;

    @ActField(name = "案件环节标题")
    @NotEmpty(message = "案件环节标题不能为空")
    private String title;

    @NotEmpty(message = "是否有罪")
    @ActField(name = "是否有罪",isJudg = true)
    private int guilty;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getGuilty() {
        return guilty;
    }

    public void setGuilty(int guilty) {
        this.guilty = guilty;
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
}
