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
        List<KVEntity> res=gjkvDao.queryList(new HashMap<>());
        for (KVEntity entity:res){
            kv.put(entity.en, entity.cn);
        }
    }

    @PreDestroy
    public void destroy() {
        //系统运行结束
        kv.clear();
    }
}
