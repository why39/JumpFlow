package com.hxy.provenance.logparser;

import com.hxy.modules.activiti.annotation.ActField;
import com.hxy.modules.activiti.annotation.ActTable;
import com.hxy.modules.common.entity.ActivitiBaseEntity;

import java.util.HashMap;
import java.util.List;

/**
 * 类的功能描述.
 * 请假demo
 *
 * @Auther hxy
 * @Date 2017/7/27
 */
@ActTable(tableName = "AJ_YX_AJ", pkName = "BMSAH")
public class GJAJEntity {
    private String BMSAH;
    private String TYSAH;
    private String AJMC;
    private String CBDW_MC;
    private String AJLB_MC;

    public String getAJLB_MC() {
        return AJLB_MC;
    }

    public void setAJLB_MC(String AJLB_MC) {
        this.AJLB_MC = AJLB_MC;
    }

    public String getCBDW_MC() {
        return CBDW_MC;
    }

    public void setCBDW_MC(String CBDW_MC) {
        this.CBDW_MC = CBDW_MC;
    }

    public String getBMSAH() {
        return BMSAH;
    }

    public void setBMSAH(String BMSAH) {
        this.BMSAH = BMSAH;
    }

    public String getTYSAH() {
        return TYSAH;
    }

    public void setTYSAH(String TYSAH) {
        this.TYSAH = TYSAH;
    }

    public String getAJMC() {
        return AJMC;
    }

    public void setAJMC(String AJMC) {
        this.AJMC = AJMC;
    }
}
