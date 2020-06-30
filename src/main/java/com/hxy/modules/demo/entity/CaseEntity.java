package com.hxy.modules.demo.entity;

import com.hxy.modules.activiti.annotation.ActField;
import com.hxy.modules.activiti.annotation.ActTable;
import com.hxy.modules.common.entity.ActivitiBaseEntity;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.HashMap;
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
    public static final String PROPERTY_PRIX = "prop_";

    private String id;
    private String title;

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

    @ActField(name = "移送案由")
    private String prop_move_reason;//移送案由

    @ActField(name = "是否共同犯罪")
    private String prop_is_together;//是否共同犯罪

    @ActField(name = "是否主犯")
    private String prop_is_main;//是否主犯

    @ActField(name = "是否未成年人")
    private String prop_is_juveniles;//是否未成年人

    @ActField(name = "是否未单位犯罪")
    private String prop_is_unit;//是否未单位犯罪

    @ActField(name = "犯罪嫌疑人姓名")
    private String prop_main_name;//犯罪嫌疑人姓名/单位名称

    @ActField(name = "犯罪嫌疑人作案时年龄")
    private String prop_main_age;// "犯罪嫌疑人作案时年龄"

    @ActField(name = "犯罪嫌疑人民族")
    private String prop_main_nation;//"犯罪嫌疑人民族"

    @ActField(name = "受教育状况")
    private String prop_main_education;// "受教育状况"

    @ActField(name = "住所地")
    private String prop_main_address;//"住所地"

    @ActField(name = "前科")
    private String prop_main_history;//"前科"

    @ActField(name = "主要作案地")
    private String prop_main_exec_address;// "主要作案地"

//    @ActField(name = "办案人")
//    private String prop_deal_name;// "办案人"

    /**
     * 案件详情
     */
    @ActField(name = "案件详情说明")
    private String leavewhy;


    private String fields;
    private List<String> filesUrl;//数据库不可能为每个文件名新建一列。一个List存文书的具体地址，还有个List用来存文书的名称


    public static HashMap<String, String> kvMap = new HashMap<>();


    /**
     * 犯罪嫌疑人姓名/单位名称
     * 犯罪嫌疑人作案时年龄
     * 犯罪嫌疑人民族
     * 受教育状况
     * 住所地
     * 移送意见
     * 前科
     * 主要作案地
     * 办案人
     */
    public void initkvMap() {
        kvMap.put("leavewhy", "案件详情说明");
        kvMap.put("rule_ourcase", "是否属于本院");

//        kvMap.put("prop_is_unit", "是否未单位犯罪");
//        kvMap.put("prop_is_juveniles", "是否未成年人");
//        kvMap.put("prop_is_main", "是否主犯");
//        kvMap.put("prop_is_together", "是否共同犯罪");
        kvMap.put("prop_move_reason", "移送意见");
        kvMap.put("prop_main_name", "犯罪嫌疑人姓名");
        kvMap.put("prop_main_age", "犯罪嫌疑人作案时年龄");
        kvMap.put("prop_main_nation", "犯罪嫌疑人民族");
        kvMap.put("prop_main_education", "受教育状况");
        kvMap.put("prop_main_address", "住所地");
        kvMap.put("prop_main_history", "前科");
        kvMap.put("prop_main_exec_address", "主要作案地");
//        kvMap.put("prop_deal_name", "办案人");

//        kvMap.put("file_cljg", "《人民监督员监督案件处理结果通知书》");
//        kvMap.put("file_bjyj", "《人民监督员表决意见通知书》");
//        kvMap.put("file_tzs", "《人民监督员监督案件通知书》");
//        kvMap.put("file_ysh", "《移送函》");
//        kvMap.put("file_spb", "《补充移送材料通知书》");
//        kvMap.put("file_djb", "《人民监督员监督案件受理登记表》");

    }

    public CaseEntity() {
        initkvMap();
    }

    public HashMap<String, String> getKvMap() {
        return kvMap;
    }

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

    public String getprop_move_reason() {
        return prop_move_reason;
    }

    public void setprop_move_reason(String prop_move_reason) {
        this.prop_move_reason = prop_move_reason;
    }

    public String getprop_is_together() {
        return prop_is_together;
    }

    public void setprop_is_together(String prop_is_together) {
        this.prop_is_together = prop_is_together;
    }

    public String getprop_is_main() {
        return prop_is_main;
    }

    public void setprop_is_main(String prop_is_main) {
        this.prop_is_main = prop_is_main;
    }

    public String getprop_is_juveniles() {
        return prop_is_juveniles;
    }

    public void setprop_is_juveniles(String prop_is_juveniles) {
        this.prop_is_juveniles = prop_is_juveniles;
    }

    public String getprop_is_unit() {
        return prop_is_unit;
    }

    public void setprop_is_unit(String prop_is_unit) {
        this.prop_is_unit = prop_is_unit;
    }

    public String getprop_main_name() {
        return prop_main_name;
    }

    public void setprop_main_name(String prop_main_name) {
        this.prop_main_name = prop_main_name;
    }

    public String getprop_main_age() {
        return prop_main_age;
    }

    public void setprop_main_age(String prop_main_age) {
        this.prop_main_age = prop_main_age;
    }

    public String getprop_main_nation() {
        return prop_main_nation;
    }

    public void setprop_main_nation(String prop_main_nation) {
        this.prop_main_nation = prop_main_nation;
    }

    public String getprop_main_education() {
        return prop_main_education;
    }

    public void setprop_main_education(String prop_main_education) {
        this.prop_main_education = prop_main_education;
    }

    public String getprop_main_address() {
        return prop_main_address;
    }

    public void setprop_main_address(String prop_main_address) {
        this.prop_main_address = prop_main_address;
    }

    public String getprop_main_history() {
        return prop_main_history;
    }

    public void setprop_main_history(String prop_main_history) {
        this.prop_main_history = prop_main_history;
    }

    public String getprop_main_exec_address() {
        return prop_main_exec_address;
    }

    public void setprop_main_exec_address(String prop_main_exec_address) {
        this.prop_main_exec_address = prop_main_exec_address;
    }

//    public String getprop_deal_name() {
//        return prop_deal_name;
//    }
//
//    public void setprop_deal_name(String prop_deal_name) {
//        this.prop_deal_name = prop_deal_name;
//    }
}
