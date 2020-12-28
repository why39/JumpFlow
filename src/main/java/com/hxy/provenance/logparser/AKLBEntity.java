package com.hxy.provenance.logparser;

import com.hxy.modules.activiti.annotation.ActTable;

/**
 * 类的功能描述.
 * 案件类别案卡项
 *
 * @Auther zyq
 * @Date 2020/12/27
 */
@ActTable(tableName = "properties_mapper", pkName = "en")
public class AKLBEntity {
    private String en;
    private String cn;
    private String category;

    public String getEn() {
        return en;
    }
    public void setEn(String en){
        this.en = en;
    }
    public String getCn() {
        return cn;
    }
    public void setCn(String cn){
        this.cn = cn;
    }
    public String getCategory(){
        return category;
    }
    public void setCategory(String category){
        this.category = category;
    }
}
