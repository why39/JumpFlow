package com.hxy.provenance.neo4j;

import java.io.Serializable;

public class CaseProvDataBean implements Serializable {
    private String case_id;
    private String case_prov_data;

    public CaseProvDataBean() {
    }

    public CaseProvDataBean(String case_id, String case_prov_data) {
        this.case_id = case_id;
        this.case_prov_data = case_prov_data;
    }

    public String getCase_id() {
        return case_id;
    }

    public void setCase_id(String case_id) {
        this.case_id = case_id;
    }

    public String getCase_prov_data() {
        return case_prov_data;
    }

    public void setCase_prov_data(String case_prov_data) {
        this.case_prov_data = case_prov_data;
    }
}
