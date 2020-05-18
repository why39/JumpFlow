package com.hxy.modules.activiti.service.impl;

import com.hxy.modules.activiti.dto.ProcessTaskDto;
import com.hxy.modules.activiti.service.JumpService;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ExecutionQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.impl.pvm.ReadOnlyProcessDefinition;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.runtime.Execution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("jumpServiceImpl")
public class JumpServiceImpl implements JumpService {

    @Autowired
    private TaskService taskService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;

    @Override
    public void jumpEndActivity(String processDefinitionId, ProcessTaskDto processTaskDto, String processInstanceId, String targetActivityId){
        Task task = taskService.createTaskQuery().taskId(processTaskDto.getTaskId()).singleResult();
        ActivityImpl currentActivity = qureyCurrentTask(processInstanceId,processDefinitionId);
        ActivityImpl targetActivity = queryTargetActivity(targetActivityId, processDefinitionId);
        //通过活动可以获得流程 将要出去的路线，只要更改出去的目的Activity ，就可以实现自由的跳转
        List<PvmTransition> outgoingTransitions = currentActivity.getOutgoingTransitions();
        Map<PvmTransition,ActivityImpl> tmpDestination = new HashMap<>();
        for (PvmTransition pvmTransition : outgoingTransitions) {
            TransitionImpl transitionImpl= (TransitionImpl)pvmTransition;
            tmpDestination.put(pvmTransition, transitionImpl.getDestination());
            transitionImpl.setDestination(targetActivity);
        }
        taskService.complete(task.getId());
        //跳转后重置原节点的边的后续节点
        for (PvmTransition pvmTransition : outgoingTransitions) {
            TransitionImpl transitionImpl= (TransitionImpl)pvmTransition;
            transitionImpl.setDestination(tmpDestination.get(pvmTransition));
        }
    }

    @Override
    public ActivityImpl queryTargetActivity(String id, String processDefinitionId){
        ReadOnlyProcessDefinition deployedProcessDefinition = (ProcessDefinitionEntity)((RepositoryServiceImpl)repositoryService).getDeployedProcessDefinition(processDefinitionId);
        List<ActivityImpl> activities = (List<ActivityImpl>) deployedProcessDefinition.getActivities();
        for (ActivityImpl activityImpl : activities) {
            if(activityImpl.getId().equals(id)){
                return activityImpl;
            }
        }
        return null;
    }

    @Override
    public ActivityImpl qureyCurrentTask(String processInstanceId, String processDefinitionId){
        Execution execution = runtimeService.createExecutionQuery().processInstanceId(processInstanceId).singleResult();
        String activityId = execution.getActivityId();
        ActivityImpl currentActivity = queryTargetActivity(activityId, processDefinitionId);
        return currentActivity;
    }

    @Override
    public  List<ActivityImpl> getActIdCollection(String processDefinitionId) {
        List<PvmActivity> pvmActivities=new ArrayList<PvmActivity>();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
        ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl)repositoryService).getDeployedProcessDefinition(processDefinition.getId());
        //获取所有节点
        List<ActivityImpl> activitiList = def.getActivities();
        return activitiList;
    }
}
