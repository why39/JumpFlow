package com.hxy.modules.activiti.service;
import com.hxy.modules.activiti.entity.RuleEntity;

import java.util.List;

public interface RuleService {

    List<RuleEntity> queryRulesAva(String modelId, String deployId, String startEvent);

    List<RuleEntity> queryRulesAll(String modelId, String deployId);

    void save(RuleEntity rule);

    void delete(String id);

    boolean compareRule(String e1, String e2);
}
