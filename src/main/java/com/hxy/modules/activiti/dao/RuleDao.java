package com.hxy.modules.activiti.dao;

import com.hxy.modules.activiti.entity.RuleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RuleDao {
    /**
     * 根据流程模型id，流程实例id和活动id 获取当前活动的所有规则
     * @param modelId
     * @param deploymentId
     * @param startEvent
     * @return
     */
    List<RuleEntity> queryRulesAva(@Param("modelId") String modelId, @Param("deploymentId") String deploymentId, @Param("startEvent") String startEvent);

    /**
     * 根据流程模型id，流程实例id, 获取当前实例的所有规则
     * @param modelId
     * @param deploymentId
     * @return
     */
    List<RuleEntity> queryRulesAll(@Param("modelId") String modelId, @Param("deploymentId") String deploymentId);


    int save(RuleEntity ruleEntity);

    int delete(String id);
}
