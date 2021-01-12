package com.hxy.provenance.logparser;

import com.hxy.modules.activiti.annotation.ActTable;

@ActTable(tableName = "gruop_list",pkName = "group_id")
public class GroupNameEntity {
    private int group_id;
    private String member_name;
    private String type;

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public int getGroup_id() {
        return group_id;
    }
    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }
    public String getMember() {
        return member_name;
    }

    public void setMember(String member_name) {
        this.member_name = member_name;
    }
}
