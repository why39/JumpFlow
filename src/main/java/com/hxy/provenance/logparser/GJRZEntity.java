package com.hxy.provenance.logparser;

import com.hxy.modules.activiti.annotation.ActTable;

@ActTable(tableName = "RZ_YX_RZ", pkName = "ID")
public class GJRZEntity {
    private String ID;
    private String BMSAH;
    private String RZMS; //日志明细
    private String CZRM; //人名
    private String ZHXGSJ;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getBMSAH() {
        return BMSAH;
    }

    public void setBMSAH(String BMSAH) {
        this.BMSAH = BMSAH;
    }

    public String getRZMS() {
        return RZMS;
    }

    public void setRZMS(String RZMS) {
        this.RZMS = RZMS;
    }

    public String getCZRM() {
        return CZRM;
    }

    public void setCZRM(String CZRM) {
        this.CZRM = CZRM;
    }

    public String getZHXGSJ() {
        return ZHXGSJ;
    }

    public void setZHXGSJ(String ZHXGSJ) {
        this.ZHXGSJ = ZHXGSJ;
    }
}
