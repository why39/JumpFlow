package com.hxy.provenance.logparser;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.List;

@Component
public class KVCache {
    @Autowired
    GJKVDao gjkvDao;

    public static HashMap<String, KVEntity> kv = new HashMap<>();

    public void preLoad(String en,String cn,String category) {
        KVEntity entity = new KVEntity();
        entity.en=en;
        entity.cn=cn;
        entity.category=category;
        kv.put(en.toUpperCase(), entity);
    }
    
    @PostConstruct
    public void init() {

        List<KVEntity> res = gjkvDao.queryList(new HashMap<>());
        for (KVEntity entity : res) {
            kv.put(entity.en.toUpperCase(), entity);
        }

        
        preLoad("ACTION","操作","其他");
        preLoad("CBDW","承办单位","案件相关");
        preLoad("AJLX","案件类型","案件相关");
        preLoad("AJLXMC","案件类型名称","案件相关");
        preLoad("OPERATOR","操作人","人员相关");
        preLoad("LOGTIMESTAMP","日志时间","其他");
        preLoad("LOGLEVEL","日志等级","其他");
        preLoad("LOGGROUPID","日志组ID","其他");
        preLoad("LOGORDER","日志优先级","其他");
        preLoad("SERVICENAME","服务名称","其他");
        preLoad("SERVICEVERSION","服务版本","其他");
        preLoad("SERVICEORDER","服务优先级","其他");
        preLoad("SJBS","审结不诉","其他");
        preLoad("TABLENAME","表名","其他");
        preLoad("RJRBH","入卷人编号","人员相关");
        preLoad("GLZRRBH","关联自然人编号","人员相关");
        preLoad("CHRBH","撤回人编号","人员相关");
        preLoad("CHRXM","撤回人姓名","人员相关");
        preLoad("BCRXM","补充人姓名","人员相关");
        preLoad("BCRBH","补充人编号","人员相关");
        preLoad("YJDQRQ","移交日期","其他");
    }

    @PreDestroy
    public void destroy() {
        //系统运行结束
        kv.clear();
    }
}
