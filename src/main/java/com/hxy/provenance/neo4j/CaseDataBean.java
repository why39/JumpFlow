package com.hxy.provenance.neo4j;

import io.swagger.annotations.ApiModel;

import java.io.Serializable;

@ApiModel("案件实体")
public class CaseDataBean implements Serializable {
    /**
     * 案件id
     */
    private String case_id;

    /**
     * 案件名称
     */
    private String case_name;

    /**
     * 案件类型
     */
    private String case_category;

    /**
     * 案件发起人id
     */
    private String user_id;

    /**
     * 案件发起人名称
     */
    private String user_name;

    /**
     * 所属部门id
     */
    private String department_id;

    /**
     * 所属部门名称
     */
    private String department_name;

    public String getCase_id() {
        return case_id;
    }

    public void setCase_id(String case_id) {
        this.case_id = case_id;
    }

    public String getCase_name() {
        return case_name;
    }

    public void setCase_name(String case_name) {
        this.case_name = case_name;
    }

    public String getCase_category() {
        return case_category;
    }

    public void setCase_category(String case_category) {
        this.case_category = case_category;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(String department_id) {
        this.department_id = department_id;
    }

    public String getDepartment_name() {
        return department_name;
    }

    public void setDepartment_name(String department_name) {
        this.department_name = department_name;
    }
}
