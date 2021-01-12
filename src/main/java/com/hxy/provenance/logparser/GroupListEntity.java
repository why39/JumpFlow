package com.hxy.provenance.logparser;

import com.hxy.modules.activiti.annotation.ActTable;

@ActTable(tableName = "gruop_list",pkName = "group_id")
public class GroupListEntity {
    private String group_name;
    private int group_id;
    private int member_count;

    public String getGroup() {
        return group_name;
    }
    public void setGroup(String group_name) {
        this.group_name = group_name;
    }

    public int getGroup_id() {
        return group_id;
    }
    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }
    public int getMember() {
        return member_count;
    }

    public void setMember(int member_count) {
        this.member_count = member_count;
    }
}
