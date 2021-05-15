package com.hxy.provenance.logparser;

public class DqResEntity {
    public String rule;
    public String res;

    public DqResEntity(String rule, String res) {
        this.rule = rule;
        this.res = res;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }
}
