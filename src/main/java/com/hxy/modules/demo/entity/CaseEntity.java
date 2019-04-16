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

    public static final String FILE_PRIX = "file_";
    public static final String RULE_PRIX = "rule_";

    private String id;

    private String userId;

    @ActField(name = "《人民监督员监督案件受理登记表》")
    private String file_djb;//受理环节

    @ActField(name = "《补充移送材料通知书》")
    private String file_bccl;//审查环节

    @ActField(name = "《人民监督员监督案件审批表》")
    private String file_spb;//审查环节

    @ActField(name = "《移送函》")
    private String file_ysh;//移送环节

    @ActField(name = "《人民监督员监督案件通知书》")
    private String file_tzs;//组织评议环节

    @ActField(name = "《人民监督员表决意见通知书》")
    private String file_bjyj;//告知表决意见

    @ActField(name = "《人民监督员监督案件处理结果通知书》")
    private String file_cljg;//反馈处理结果环节

    //wxp:isJudg用于添加规则判断
    @ActField(name = "是否属于本院", isJudg = true)
    private int rule_ourcase;//是否属于本院

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

    public String getFile_djb() {
        return file_djb;
    }

    public void setFile_djb(String file_djb) {
        this.file_djb = file_djb;
    }

    public String getFile_bccl() {
        return file_bccl;
    }

    public void setFile_bccl(String file_bccl) {
        this.file_bccl = file_bccl;
    }

    public String getFile_spb() {
        return file_spb;
    }

    public void setFile_spb(String file_spb) {
        this.file_spb = file_spb;
    }

    public String getFile_ysh() {
        return file_ysh;
    }

    public void setFile_ysh(String file_ysh) {
        this.file_ysh = file_ysh;
    }

    public String getFile_tzs() {
        return file_tzs;
    }

    public void setFile_tzs(String file_tzs) {
        this.file_tzs = file_tzs;
    }

    public String getFile_bjyj() {
        return file_bjyj;
    }

    public void setFile_bjyj(String file_bjyj) {
        this.file_bjyj = file_bjyj;
    }

    public String getFile_cljg() {
        return file_cljg;
    }

    public void setFile_cljg(String file_cljg) {
        this.file_cljg = file_cljg;
    }

    public int getRule_ourcase() {
        return rule_ourcase;
    }

    public void setRule_ourcase(int rule_ourcase) {
        this.rule_ourcase = rule_ourcase;
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    public List<String> getFilesUrl() {
        return filesUrl;
    }

    public void setFilesUrl(List<String> filesUrl) {
        this.filesUrl = filesUrl;
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
}
