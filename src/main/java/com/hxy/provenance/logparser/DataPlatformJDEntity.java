package com.hxy.provenance.logparser;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;




public class DataPlatformJDEntity {

    public String retCode;
    public String retMsg;
    public List<Item> data;


    public static class Item {


        @JsonProperty("cjsj")
        private String cjsj;
        @JsonProperty("dwbm")
        private String dwbm;
        @JsonProperty("jdjrsj")
        private String jdjrsj;
        @JsonProperty("jdlksj")
        private String jdlksj;
        @JsonProperty("jdjryy")
        private String jdjryy;
        @JsonProperty("jdlx")
        private String jdlx;
        @JsonProperty("jdsxh")
        private Object jdsxh;
        @JsonProperty("jdzxzbm")
        private String jdzxzbm;
        @JsonProperty("jdzxzxm")
        private String jdzxzxm;
        @JsonProperty("sjbsbh")
        private String sjbsbh;
        @JsonProperty("jdzxzt")
        private String jdzxzt;
        @JsonProperty("lcjdbh")
        private String lcjdbh;
        @JsonProperty("lcjdmc")
        private String lcjdmc;
        @JsonProperty("lcmbbm")
        private String lcmbbm;
        @JsonProperty("lcslbh")
        private String lcslbh;
        @JsonProperty("lcsljdbh")
        private String lcsljdbh;
        @JsonProperty("lcsljdxh")
        private Integer lcsljdxh;
        @JsonProperty("lczt")
        private Object lczt;
        @JsonProperty("sfsc")
        private String sfsc;
        @JsonProperty("sjly")
        private String sjly;
        @JsonProperty("tysah")
        private Object tysah;
        @JsonProperty("bmsah")
        private String bmsah;
        @JsonProperty("zhxgsj")
        private String zhxgsj;

        public String getCjsj() {
            return cjsj;
        }

        public void setCjsj(String cjsj) {
            this.cjsj = cjsj;
        }

        public String getDwbm() {
            return dwbm;
        }

        public void setDwbm(String dwbm) {
            this.dwbm = dwbm;
        }

        public String getJdjrsj() {
            return jdjrsj;
        }

        public void setJdjrsj(String jdjrsj) {
            this.jdjrsj = jdjrsj;
        }

        public String getJdlksj() {
            return jdlksj;
        }

        public void setJdlksj(String jdlksj) {
            this.jdlksj = jdlksj;
        }

        public String getJdjryy() {
            return jdjryy;
        }

        public void setJdjryy(String jdjryy) {
            this.jdjryy = jdjryy;
        }

        public String getJdlx() {
            return jdlx;
        }

        public void setJdlx(String jdlx) {
            this.jdlx = jdlx;
        }

        public Object getJdsxh() {
            return jdsxh;
        }

        public void setJdsxh(Object jdsxh) {
            this.jdsxh = jdsxh;
        }

        public String getJdzxzbm() {
            return jdzxzbm;
        }

        public void setJdzxzbm(String jdzxzbm) {
            this.jdzxzbm = jdzxzbm;
        }

        public String getJdzxzxm() {
            return jdzxzxm;
        }

        public void setJdzxzxm(String jdzxzxm) {
            this.jdzxzxm = jdzxzxm;
        }

        public String getSjbsbh() {
            return sjbsbh;
        }

        public void setSjbsbh(String sjbsbh) {
            this.sjbsbh = sjbsbh;
        }

        public String getJdzxzt() {
            return jdzxzt;
        }

        public void setJdzxzt(String jdzxzt) {
            this.jdzxzt = jdzxzt;
        }

        public String getLcjdbh() {
            return lcjdbh;
        }

        public void setLcjdbh(String lcjdbh) {
            this.lcjdbh = lcjdbh;
        }

        public String getLcjdmc() {
            return lcjdmc;
        }

        public void setLcjdmc(String lcjdmc) {
            this.lcjdmc = lcjdmc;
        }

        public String getLcmbbm() {
            return lcmbbm;
        }

        public void setLcmbbm(String lcmbbm) {
            this.lcmbbm = lcmbbm;
        }

        public String getLcslbh() {
            return lcslbh;
        }

        public void setLcslbh(String lcslbh) {
            this.lcslbh = lcslbh;
        }

        public String getLcsljdbh() {
            return lcsljdbh;
        }

        public void setLcsljdbh(String lcsljdbh) {
            this.lcsljdbh = lcsljdbh;
        }

        public Integer getLcsljdxh() {
            return lcsljdxh;
        }

        public void setLcsljdxh(Integer lcsljdxh) {
            this.lcsljdxh = lcsljdxh;
        }

        public Object getLczt() {
            return lczt;
        }

        public void setLczt(Object lczt) {
            this.lczt = lczt;
        }

        public String getSfsc() {
            return sfsc;
        }

        public void setSfsc(String sfsc) {
            this.sfsc = sfsc;
        }

        public String getSjly() {
            return sjly;
        }

        public void setSjly(String sjly) {
            this.sjly = sjly;
        }

        public Object getTysah() {
            return tysah;
        }

        public void setTysah(Object tysah) {
            this.tysah = tysah;
        }

        public String getBmsah() {
            return bmsah;
        }

        public void setBmsah(String bmsah) {
            this.bmsah = bmsah;
        }

        public String getZhxgsj() {
            return zhxgsj;
        }

        public void setZhxgsj(String zhxgsj) {
            this.zhxgsj = zhxgsj;
        }
    }
}
