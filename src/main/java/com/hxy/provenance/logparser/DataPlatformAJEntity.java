package com.hxy.provenance.logparser;

import java.util.Date;
import java.util.List;

public class DataPlatformAJEntity {

    public String retCode;
    public String retMsg;
    public List<Item> data;

    public static class Item {
        /**
         * searchValue : null
         * createBy : null
         * createTime : null
         * updateBy : null
         * updateTime : null
         * remark : null
         * dataScope : null
         * params : {}
         * bmsah : 汉东检刑抗诉受[2019]980000100008号
         * tysah : 98010020190091001
         * ajmc : 445666
         * ajlbmc : null
         * cbdwmc : null
         */

        private Object searchValue;
        private Object createBy;
        private Object createTime;
        private Object updateBy;
        private Object updateTime;
        private Object remark;
        private Object dataScope;
        private ParamsBean params;
        private String bmsah;
        private String tysah;
        private String ajmc;
        private String ajlbmc;
        private String cbdwmc;
        private Date cjsj;

        //20210517 Guizhou
        private String ywmc;    //业务部门
        private String cbjcg;   //承办检察官
        private String cbjcgsf; //承办检察官身份
        private String batdmc;  //办案团队名称

        private String jdjrsj;
        private String jdzxzxm;
        public String getYwmc() {
            return ywmc;
        }

        public void setYwmc(String ywmc) {
            this.ywmc = ywmc;
        }

        public String getCbjcg() {
            return cbjcg;
        }

        public void setCbjcg(String cbjcg) {
            this.cbjcg = cbjcg;
        }

        public String getCbjcgsf() {
            return cbjcgsf;
        }

        public void setCbjcgsf(String cbjcgsf) {
            this.cbjcgsf = cbjcgsf;
        }

        public String getBatdmc() {
            return batdmc;
        }

        public void setBatdmc(String batdmc) {
            this.batdmc = batdmc;
        }


        public Object getSearchValue() {
            return searchValue;
        }

        public void setSearchValue(Object searchValue) {
            this.searchValue = searchValue;
        }

        public Object getCreateBy() {
            return createBy;
        }

        public void setCreateBy(Object createBy) {
            this.createBy = createBy;
        }

        public Object getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Object createTime) {
            this.createTime = createTime;
        }

        public Object getUpdateBy() {
            return updateBy;
        }

        public void setUpdateBy(Object updateBy) {
            this.updateBy = updateBy;
        }

        public Object getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(Object updateTime) {
            this.updateTime = updateTime;
        }

        public Object getRemark() {
            return remark;
        }

        public void setRemark(Object remark) {
            this.remark = remark;
        }

        public Object getDataScope() {
            return dataScope;
        }

        public void setDataScope(Object dataScope) {
            this.dataScope = dataScope;
        }

        public ParamsBean getParams() {
            return params;
        }

        public void setParams(ParamsBean params) {
            this.params = params;
        }

        public String getBmsah() {
            return bmsah;
        }

        public void setBmsah(String bmsah) {
            this.bmsah = bmsah;
        }

        public String getTysah() {
            return tysah;
        }

        public void setTysah(String tysah) {
            this.tysah = tysah;
        }

        public String getAjmc() {
            return ajmc;
        }

        public void setAjmc(String ajmc) {
            this.ajmc = ajmc;
        }

        public String getAjlbmc() {
            return ajlbmc;
        }

        public void setAjlbmc(String ajlbmc) {
            this.ajlbmc = ajlbmc;
        }

        public String getCbdwmc() {
            return cbdwmc;
        }

        public void setCbdwmc(String cbdwmc) {
            this.cbdwmc = cbdwmc;
        }

        public static class ParamsBean {
        }

        public Date getCjsj() {
            return cjsj;
        }

        public void setCjsj(Date cjsj) {
            this.cjsj = cjsj;
        }
        public String getJdjrsj() {
            return jdjrsj;
        }

        public void setJdjrsj(String jdjrsj) {
            this.jdjrsj = jdjrsj;
        }

        public String getJdzxzxm() {
            return jdzxzxm;
        }

        public void setJdzxzxm(String jdzxzxm) {
            this.jdzxzxm = jdzxzxm;
        }
    }


}
