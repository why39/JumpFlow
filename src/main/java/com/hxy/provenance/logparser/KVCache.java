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

    public static HashMap<String, String> kv = new HashMap<>();

    @PostConstruct
    public void init() {

        List<KVEntity> res = gjkvDao.queryList(new HashMap<>());
        for (KVEntity entity : res) {
            kv.put(entity.en.toUpperCase(), entity.cn);
        }

        kv.put("ACTION","操作");
        kv.put("CBDW","承办单位");
        kv.put("AJLX","案件类型");
        kv.put("AJLXMC","案件类型名称");
        kv.put("OPERATOR","操作人");
        kv.put("LOGTIMESTAMP","日志时间");
        kv.put("LOGLEVEL","日志等级");
        kv.put("LOGGROUPID","日志组ID");
        kv.put("LOGORDER","日志优先级");
        kv.put("SERVICENAME","服务名称");
        kv.put("SERVICEVERSION","服务版本");
        kv.put("SERVICEORDER","服务优先级");
        kv.put("SJBS","审结不诉");
        kv.put("TABLENAME","表名");
        kv.put("RJRBH","入卷人编号");
        kv.put("GLZRRBH","关联自然人编号");
        kv.put("CHRBH","撤回人编号");
        kv.put("CHRXM","撤回人姓名");
        kv.put("BCRXM","补充人姓名");
        kv.put("BCRBH","补充人编号");
        kv.put("YJDQRQ","移交日期");
    }

    @PreDestroy
    public void destroy() {
        //系统运行结束
        kv.clear();
    }
}
