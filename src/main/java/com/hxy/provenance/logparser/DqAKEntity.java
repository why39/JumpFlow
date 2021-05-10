package com.hxy.provenance.logparser;

import com.hxy.modules.activiti.annotation.ActTable;

@ActTable(tableName = "tb_dq_ak", pkName = "id")
public class DqAKEntity {
    private String id;
    private String AJMC;
    private String BMSAH;
    private String AFD_MC;
    private String CBDW_MC;
    private int CBDW_BM;
    private String AJLB_MC;
    private int AJLB_BM;
    private String AY_MC;
    private int AY_DM;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBMSAH() {
        return BMSAH;
    }

    public void setBMSAH(String BMSAH) {
        this.BMSAH = BMSAH;
    }

    public String getAJMC() {
        return AJMC;
    }

    public void setAJMC(String AJMC) {
        this.AJMC = AJMC;
    }

    public String getAFD_MC() {
        return AFD_MC;
    }

    public void setAFD_MC(String AFD_MC) {
        this.AFD_MC = AFD_MC;
    }

    public String getCBDW_MC() {
        return CBDW_MC;
    }

    public void setCBDW_MC(String CBDW_MC) {
        this.CBDW_MC = CBDW_MC;
    }

    public int getCBDW_BM() {
        return CBDW_BM;
    }

    public void setCBDW_BM(int CBDW_BM) {
        this.CBDW_BM = CBDW_BM;
    }

    public String getAJLB_MC() {
        return AJLB_MC;
    }

    public void setAJLB_MC(String AJLB_MC) {
        this.AJLB_MC = AJLB_MC;
    }

    public int getAJLB_BM() {
        return AJLB_BM;
    }

    public void setAJLB_BM(int AJLB_BM) {
        this.AJLB_BM = AJLB_BM;
    }

    public String getAY_MC() {
        return AY_MC;
    }

    public void setAY_MC(String AY_MC) {
        this.AY_MC = AY_MC;
    }

    public int getAY_DM() {
        return AY_DM;
    }

    public void setAY_DM(int AY_DM) {
        this.AY_DM = AY_DM;
    }
}
