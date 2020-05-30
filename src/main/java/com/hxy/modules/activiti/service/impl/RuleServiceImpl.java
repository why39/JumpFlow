package com.hxy.modules.activiti.service.impl;

import com.hxy.modules.activiti.dao.RuleDao;
import com.hxy.modules.activiti.entity.RuleEntity;
import com.hxy.modules.activiti.service.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("ruleService")
public class RuleServiceImpl implements RuleService {
    @Autowired
    private RuleDao ruleDao;

    @Override
    public void save(RuleEntity rule){
        ruleDao.save(rule);
    }
    @Override
    public void delete(String id){
        ruleDao.delete(id);
    }
    @Override
    public List<RuleEntity> queryRulesAva(String modelId, String deployId, String startEvent){
        return ruleDao.queryRulesAva(modelId, deployId, startEvent);
    }
    @Override
    public  List<RuleEntity> queryRulesAll(String modelId, String deployId){
        return ruleDao.queryRulesAll(modelId, deployId);
    }

    @Override
    public boolean compareRule(String e1, String e2){
        Map<String, Integer> expMap = new HashMap<>();
        String[] eArray1 = e1.split("&");
        String[] eArray2 = e2.split("&");
        for(String s : eArray1){
            expMap.put(s, 1);
        }
        for(String s : eArray2){
            if(!expMap.containsKey(s)){
                return false;
            }
            expMap.put(s, 0);
        }
        for(String s : expMap.keySet()){
            if(expMap.get(s) != 0){
                return false;
            }
        }
        return true;
    }
}
