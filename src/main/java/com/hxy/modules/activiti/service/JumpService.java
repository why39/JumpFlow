package com.hxy.modules.activiti.service;

import com.hxy.modules.activiti.dto.ProcessTaskDto;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.engine.impl.pvm.process.ActivityImpl;

import java.util.Collection;
import java.util.List;

/**
 * 类的功能描述.
 * 流程执行跳转服务
 * @Auther Hanyu Wu
 * @Date 2020/4/29
 */

public interface JumpService {

    /**
     * 跳转到目标节点
     * @param processDefinitionId   流程定义id
     * @param processTaskDto        流程任务对象
     * @param processInstanceId     流程实例id
     * @param targetActivityId      目标节点id
     * @return
     */
    void jumpEndActivity(String processDefinitionId, ProcessTaskDto processTaskDto, String processInstanceId, String targetActivityId);

    /**
     *  根据ActivityId 查询出来想要活动Activity
     * @param id    ActivityId
     * @param processDefinitionId   流程定义id
     * @return
     */
    ActivityImpl queryTargetActivity(String id, String processDefinitionId);

    /**
     * 查询当前的活动节点
     * @param processInstanceId   流程实例id
     * @param processDefinitionId 流程定义id
     */
    ActivityImpl qureyCurrentTask(String processInstanceId,String processDefinitionId);

    /**
     * 获取流程中所有的活动id
     * @param processDefinitionId   流程定义id
     */
    List<ActivityImpl> getActIdCollection(String processDefinitionId);
}

