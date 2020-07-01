package com.hxy.modules.activiti.service.impl;

import com.alibaba.fastjson.JSON;
import com.hxy.modules.activiti.annotation.ActField;
import com.hxy.modules.activiti.annotation.ActTable;
import com.hxy.modules.activiti.dao.ActExtendDao;
import com.hxy.modules.activiti.dto.ProcessTaskDto;
import com.hxy.modules.activiti.entity.ExtendActBusinessEntity;
import com.hxy.modules.activiti.entity.ExtendActNodesetEntity;
import com.hxy.modules.activiti.service.ExtendActBusinessService;
import com.hxy.modules.activiti.service.ExtendActNodesetService;
import com.hxy.modules.activiti.service.JumpService;
import com.hxy.modules.activiti.utils.AnnotationUtils;
import com.hxy.modules.common.entity.TableInfo;
import com.hxy.modules.common.exception.MyException;
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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedCaseInsensitiveMap;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service("jumpServiceImpl")
public class JumpServiceImpl implements JumpService {

    @Autowired
    private TaskService taskService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private ActExtendDao actExtendDao;
    @Autowired
    private ExtendActBusinessService businessService;
    @Autowired
    private ExtendActNodesetService nodesetService;

    @Override
    public void jumpEndActivity(Map<String,Object> map,String processDefinitionId, ProcessTaskDto processTaskDto, String processInstanceId, String targetActivityId) {
        Task task = taskService.createTaskQuery().taskId(processTaskDto.getTaskId()).singleResult();
        ActivityImpl currentActivity = qureyCurrentTask(processInstanceId, processDefinitionId);
        ActivityImpl targetActivity = queryTargetActivity(targetActivityId, processDefinitionId);
        //通过活动可以获得流程 将要出去的路线，只要更改出去的目的Activity ，就可以实现自由的跳转
        List<PvmTransition> outgoingTransitions = currentActivity.getOutgoingTransitions();
        Map<PvmTransition, ActivityImpl> tmpDestination = new HashMap<>();
        for (PvmTransition pvmTransition : outgoingTransitions) {
            TransitionImpl transitionImpl = (TransitionImpl) pvmTransition;
            tmpDestination.put(pvmTransition, transitionImpl.getDestination());
            transitionImpl.setDestination(targetActivity);
        }
        taskService.complete(task.getId());
        //跳转后重置原节点的边的后续节点
        for (PvmTransition pvmTransition : outgoingTransitions) {
            TransitionImpl transitionImpl = (TransitionImpl) pvmTransition;
            transitionImpl.setDestination(tmpDestination.get(pvmTransition));
        }


        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processTaskDto.getDefId()).singleResult();
        ExtendActBusinessEntity businessEntity = businessService.queryByActKey(processDefinition.getKey());
        ExtendActNodesetEntity nodesetEntity = nodesetService.queryByNodeId(task.getTaskDefinitionKey());

        Class<?> clazz = null;
        try {
            clazz = Class.forName(businessEntity.getClassurl());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        ActTable actTable=clazz.getAnnotation(ActTable.class);
        changeFields(actTable, processTaskDto.getBusId(), nodesetEntity.getChangeFiles(), businessEntity.getClassurl(), map);
    }

    /**
     * 保存流程更改过的业务记录信息
     *
     * @param actTable
     * @param busId
     * @param changeFieldNames
     * @param classUrl
     * @param map
     * @see ActModelerServiceImpl.changeFields(ActTable actTable,String busId,String changeFieldNames,String classUrl,Map<String,Object> map)
     */
    public String changeFields(ActTable actTable, String busId, String changeFieldNames, String classUrl, Map<String, Object> map) {
        if (StringUtils.isEmpty(changeFieldNames)) {
            return "";
        }
        //更改的业务字段文本描述
        StringBuffer filedText = new StringBuffer();
        //查询业务记录
        Map<String, Object> tamap = new HashMap<>();
        tamap.put(TableInfo.TAB_TABLENAME, actTable.tableName());
        tamap.put(TableInfo.TAB_PKNAME, actTable.pkName());
        tamap.put(TableInfo.TAB_ID, busId);
        Map<String, Object> busInfo = actExtendDao.queryBusiByBusId(tamap);
        Map<String, String> textMap = new HashMap<>();
        List<Map<String, Object>> mapList = AnnotationUtils.getActFieldByClazz(classUrl);
        for (Map remap : mapList) {
            ActField actField = (ActField) remap.get("actField");
            String keyName = (String) remap.get("keyName");
            if (actField != null) {
                textMap.put(keyName, actField.name());
            }
        }
        //业务可更改的字段
        String[] changeFields = changeFieldNames.split(",");
        //业务可更改的字段和对应更改后的值
        List<TableInfo> fields = new ArrayList<>();
        Map<String, Object> filedsContent = new HashMap<>();

        for (int i = 0; i < changeFields.length; i++) {
            if (StringUtils.isEmpty(changeFields[i])) {
                continue;
            }
            //原值
            Object o = busInfo.get(changeFields[i]);
            //更改后的值
            Object o1 = map.get(changeFields[i]);
            //字段text 例：请假天数
            String text = textMap.get(changeFields[i]);
            if (o instanceof String) {
                if (!o.equals(o1)) {
                    filedText.append(text + "的原值【" + o + "】,更改后【" + o1 + "】;");
                }
            } else if (o instanceof Integer) {
                if (((Integer) o).intValue() != Integer.parseInt(((String) o1))) {
                    filedText.append(text + "的原值为【" + o + "】,更改为【" + o1 + "】;");
                }
            }
            filedsContent.put(changeFields[i], map.get(changeFields[i]));
            fields.add(new TableInfo(changeFields[i], map.get(changeFields[i])));
        }
        //保存业务更改后的值
        Map<String, Object> params = new HashMap<>();
        params.put(TableInfo.TAB_TABLENAME, actTable.tableName());
        params.put(TableInfo.TAB_PKNAME, actTable.pkName());
        params.put(TableInfo.TAB_ID, busId);
        //将fields以json存放到files列中。

        params.put(TableInfo.TAB_FIELDS, JSON.toJSONString(filedsContent));//params.put(TableInfo.TAB_FIELDS,fields);
        int count = actExtendDao.updateChangeBusInfoBatch(params);//actExtendDao.updateChangeBusInfo(params);
        if (count < 1) {
            throw new MyException("更新业务信息失败");
        }
        return filedText.toString();
    }

    @Override
    public ActivityImpl queryTargetActivity(String id, String processDefinitionId) {
        ReadOnlyProcessDefinition deployedProcessDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition(processDefinitionId);
        List<ActivityImpl> activities = (List<ActivityImpl>) deployedProcessDefinition.getActivities();
        for (ActivityImpl activityImpl : activities) {
            if (activityImpl.getId().equals(id)) {
                return activityImpl;
            }
        }
        return null;
    }

    @Override
    public ActivityImpl qureyCurrentTask(String processInstanceId, String processDefinitionId) {
        Execution execution = runtimeService.createExecutionQuery().processInstanceId(processInstanceId).singleResult();
        String activityId = execution.getActivityId();
        ActivityImpl currentActivity = queryTargetActivity(activityId, processDefinitionId);
        return currentActivity;
    }

    @Override
    public List<ActivityImpl> getActIdCollection(String processDefinitionId) {
        List<PvmActivity> pvmActivities = new ArrayList<PvmActivity>();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
        ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition(processDefinition.getId());
        //获取所有节点
        List<ActivityImpl> activitiList = def.getActivities();
        return activitiList;
    }
}
