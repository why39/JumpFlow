package com.hxy.provenance.logparser;

import com.hxy.modules.activiti.annotation.ActTable;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


/**
 * 类的功能描述.
 * 请假demo
 *
 * @Auther hxy
 * @Date 2017/7/27
 */
@ActTable(tableName = "aj_yx_aj", pkName = "BMSAH")
public class GJAJEntity {
    private String BMSAH;   //部门受案号
    private String TYSAH;
    private String AJMC;    //案件名称
    private String AJLB_MC; // 案件类别名称
    private String CBDW_MC;
    private int IS_COMPLETE = 0;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date CJSJ;

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

    public void setCJSJ(Date CJSJ) {
        this.CJSJ = CJSJ;
    }
    public Date getCJSJ() {
        return CJSJ;
    }

    public void setIS_COMPLETE(int IS_COMPLETE) {
        this.IS_COMPLETE = IS_COMPLETE;
    }
    public int getIS_COMPLETE() {
        return IS_COMPLETE;
    }

    public void setAJMC(String AJMC) {
        this.AJMC = AJMC;
    }
}
