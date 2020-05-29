package com.hxy.modules.activiti.service.impl;

import com.hxy.modules.activiti.dao.RuleDao;
import com.hxy.modules.activiti.entity.RuleEntity;
import com.hxy.modules.activiti.service.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
