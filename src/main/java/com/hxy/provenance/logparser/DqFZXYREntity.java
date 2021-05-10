package com.hxy.provenance.logparser;

import com.hxy.modules.activiti.annotation.ActTable;

@ActTable(tableName = "tb_dq_fzxyr", pkName = "ID")
public class DqFZXYREntity {
    private String id;
    private String xyrxm;//人名
    private int xyrbh; //编号
    private int afsnl; //案发时年龄
    private String xb; //male， female

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getXyrxm() {
        return xyrxm;
    }

    public void setXyrxm(String xyrxm) {
        this.xyrxm = xyrxm;
    }

    public int getXyrbh() {
        return xyrbh;
    }

    public void setXyrbh(int xyrbh) {
        this.xyrbh = xyrbh;
    }

    public int getAfsnl() {
        return afsnl;
    }

    public void setAfsnl(int afsnl) {
        this.afsnl = afsnl;
    }

    public String getXb() {
        return xb;
    }

    public void setXb(String xb) {
        this.xb = xb;
    }
}
