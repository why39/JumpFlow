package com.hxy.provenance.logparser;

import com.hxy.modules.activiti.annotation.ActTable;

@ActTable(tableName = "RZ_YX_RZ", pkName = "ID")
public class GJRZEntity {
    private String ID;
    private String BMSAH;
    private String RZMS; //日志明细
    private String CZRM; //人名
    private String ZHXGSJ; //最后修改使劲
    private String EJFL; //日志类别，TYYW_LCBA_YW_BL_LC_XXX:流程节点信息。
    public static final String LC_PREFIX = "TYYW_LCBA_YW_BL_LC_";

    public String getEJFL() {
        return EJFL;
    }

    public void setEJFL(String EJFL) {
        this.EJFL = EJFL;
    }

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
