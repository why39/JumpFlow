package com.hxy.modules.activiti.controller;

import com.alibaba.fastjson.JSONObject;
import com.hxy.modules.activiti.dto.ProcessNodeDto;
import com.hxy.modules.activiti.dto.ProcessTaskDto;
import com.hxy.modules.activiti.entity.ExtendActFlowbusEntity;
import com.hxy.modules.activiti.entity.ExtendActModelEntity;
import com.hxy.modules.activiti.entity.ExtendActNodesetEntity;
import com.hxy.modules.activiti.entity.ExtendActTasklogEntity;
import com.hxy.modules.activiti.service.*;
import com.hxy.modules.activiti.service.impl.JumpServiceImpl;
import com.hxy.modules.common.exception.MyException;
import com.hxy.modules.common.page.Page;
import com.hxy.modules.common.utils.*;
import com.hxy.modules.demo.entity.CaseEntity;
import com.hxy.modules.sys.entity.UserEntity;
import com.hxy.provenance.neo4j.CaseDataNodeBean;
import com.hxy.provenance.neo4j.Neo4jFinalUtil;
import com.inesa.neo4j.Neo4jUtil;
import okhttp3.*;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedCaseInsensitiveMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 类的功能描述.
 * 流程办理相关操作
 *
 * @Auther hxy
 * @Date 2017/7/11
 */
@Controller
@RequestMapping("act/deal")
public class ExtendActDealController {
    private Logger log = Logger.getLogger(getClass());

    @Autowired
    ExtendActModelerService extendActModelService;

    @Autowired
    ActModelerService actModelerService;

    @Autowired
    ExtendActNodesetService nodesetService;

    @Autowired
    ExtendActTasklogService tasklogService;

    @Autowired
    TaskService taskService;

    @Autowired
    ExtendActFlowbusService flowbusService;

    @Autowired
    JumpService jumpService;

    /**
     * 列表
     *
     * @param model
     * @param actModelEntity
     * @param request
     * @return
     */
    @RequestMapping("list")
    public String list(Model model, ExtendActModelEntity actModelEntity, HttpServletRequest request) {
        int pageNum = CommUtils.parseInt(request.getParameter("pageNum"), 1);
        Page<ExtendActModelEntity> page = extendActModelService.findPage(actModelEntity, pageNum);
        model.addAttribute("page", page);
        model.addAttribute("model", actModelEntity);
        return "activiti/extendActModelList";
    }

    @RequestMapping("query")
    public String query() {
        return "neod.html";
    }

    /**
     * 根据流程key 获取业务可用的流程
     *
     * @param model
     * @param actKey
     * @param busId
     * @return
     */
    @RequestMapping("queryFlowsByActKey")
    public String queryFlowsByActKey(Model model, String actKey, String busId) {
        List<Map<String, Object>> defs = actModelerService.queryFlowsByActKey(actKey);
        model.addAttribute("defList", defs);
        model.addAttribute("busId", busId);
        model.addAttribute("actKey", actKey);
        return "activiti/flowSubmit";
    }

    /**
     * 获取当前节点可选择的审批人
     *
     * @param model
     * @param actKey
     * @param busId
     * @return
     */
    @RequestMapping("getUsers")
    public String getUsers(Model model, String actKey, String busId) {
        List<Map<String, Object>> defs = actModelerService.queryFlowsByActKey(actKey);
        model.addAttribute("defList", defs);
        model.addAttribute("busId", busId);
        model.addAttribute("actKey", actKey);
        return "activiti/flowSubmit";
    }

    /**
     * 获取流程第一个节点信息
     *
     * @param deployId 部署id
     * @return
     */
    @RequestMapping(value = "getStartFlowInfo", method = RequestMethod.POST)
    @ResponseBody
    public Result getStartFlowInfo(String deployId) {
        Result result = new Result();
        try {
            result = actModelerService.getStartFlowInfo(deployId);
        } catch (IOException e) {
            e.printStackTrace();
            result = Result.error("获取第一个节点信息失败!");
        }
        return result;
    }

    /**
     * 流程选择审批人窗口
     *
     * @param nodeId
     * @param user
     * @return
     */
    @RequestMapping(value = "userWindow")
    public String userWindow(String nodeId, String nodeAction, HttpServletRequest request, UserEntity user, Model model) {
        int pageNum = CommUtils.parseInt(request.getParameter("pageNum"), 1);
        Page<UserEntity> mapPage = actModelerService.userWindowPage(nodeId, pageNum, user.getUserName());
        model.addAttribute("page", mapPage);
        model.addAttribute("url", "/act/deal/userWindow");
        model.addAttribute("flag", nodeAction);
        model.addAttribute("user", user);
        return "activiti/userWindow";
    }

    /**
     * 转办变更人选择弹框
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "turnUserWindow")
    public String turnUserWindow(UserEntity user, HttpServletRequest request, Model model) {
        int pageNum = CommUtils.parseInt(request.getParameter("pageNum"), 1);
        Page<UserEntity> mapPage = actModelerService.turnWindowPage(pageNum, user);
        model.addAttribute("page", mapPage);
        //1 为单选 2为复选
        model.addAttribute("flag", "1");
        model.addAttribute("url", "/act/deal/turnUserWindow");
        model.addAttribute("user", user);
        return "activiti/userWindow";
    }

    /**
     * 启动流程
     *
     * @param processTaskDto 完成任务dto
     * @return
     */
    @RequestMapping(value = "startFlow", method = RequestMethod.POST)
    @ResponseBody
    public Result startFlow(ProcessTaskDto processTaskDto) {
        Result result = null;
        try {
            actModelerService.startFlow(processTaskDto);
            result = Result.ok("提交成功!");
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.error("提交失败!");
        }
        return result;
    }

    /**
     * 获取实时流程图
     *
     * @param processInstanceId 流程实例
     * @return
     */
    @RequestMapping(value = "showFlowImg", method = RequestMethod.GET)
    @ResponseBody
    public Result showFlowImg(String processInstanceId, HttpServletResponse response) {
        try {
            InputStream inputStream = actModelerService.getFlowImgByInstantId(processInstanceId);
            //输出到页面
            byte[] b = new byte[1024];
            int len;
            while ((len = inputStream.read(b, 0, 1024)) != -1) {
                response.getOutputStream().write(b, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 我的待办列表
     *
     * @param model
     * @param code
     * @param busId
     * @param request
     * @return
     */
    @RequestMapping("myUpcoming")
    @RequiresPermissions("act:model:myUpcoming")
    public String myUpcoming(Model model, String code, String busId, HttpServletRequest request) {
        int pageNum = CommUtils.parseInt(request.getParameter("pageNum"), 1);
        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        params.put("busId", busId);
        Page<ExtendActModelEntity> page = actModelerService.findMyUpcomingPage(params, pageNum);
        model.addAttribute("page", page);
        model.addAttribute("code", code);
        model.addAttribute("busId", busId);
        return "activiti/myUpcoming";
    }

    /**
     * 我的已办列表
     *
     * @param model
     * @param code
     * @param busId
     * @param request
     * @return
     */
    @RequestMapping("myDoneList")
    @RequiresPermissions("act:model:myUpcoming")
    public String myDoneList(Model model, String code, String busId, HttpServletRequest request) {
        int pageNum = CommUtils.parseInt(request.getParameter("pageNum"), 1);
        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        params.put("busId", busId);
        Page<ExtendActModelEntity> page = actModelerService.myDonePage(params, pageNum);
        model.addAttribute("page", page);
        model.addAttribute("code", code);
        model.addAttribute("busId", busId);
        return "activiti/myDoneList";
    }

    /**
     * 办理任务Tab
     *
     * @param model
     * @param flag    1为查看审批记录，2为办理任务
     * @param request
     * @return
     */
    @RequestMapping("flowInfoTab")
    public String flowInfoTab(String flag, ProcessTaskDto processTaskDto, Model model, HttpServletRequest request) {
        model.addAttribute("flag", flag);
        model.addAttribute("taskDto", processTaskDto);
        return "activiti/flowInfoTab";
    }

    /**
     * 流程信息
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "flowInfoHtml", method = RequestMethod.POST)
    public String flowInfoHtml(Model model, HttpServletRequest request, String busId, String instanceId) {
        Map<String, Object> params = new HashMap<>();
        params.put("busId", busId);
        List<ExtendActTasklogEntity> tasklogEntities = tasklogService.queryList(params);
        model.addAttribute("taskLogs", tasklogEntities);
        model.addAttribute("instanceId", instanceId);
        return "activiti/taskLogImg";
    }

    /**
     * 办理任务时查询业务可更改的字段和必要的流程相关信息
     *
     * @param processTaskDto
     * @return
     */
    @RequestMapping(value = "getChangeFileds", method = RequestMethod.POST)
    @ResponseBody
    public Result getChangeFileds(ProcessTaskDto processTaskDto) {
        if (StringUtils.isEmpty(processTaskDto.getTaskId())) {
            throw new MyException("任务id不能为空");
        }
        if (StringUtils.isEmpty(processTaskDto.getInstanceId())) {
            throw new MyException("流程实例id不能为空");
        }
        if (StringUtils.isEmpty(processTaskDto.getDefId())) {
            throw new MyException("流程定义id不能为空");
        }
        Task task = taskService.createTaskQuery().taskId(processTaskDto.getTaskId()).singleResult();
        //查询可更改字段
        ExtendActNodesetEntity nodesetEntity = nodesetService.queryByNodeId(task.getTaskDefinitionKey());
        //查询需要作为流程条件判断的字段
        Set<String> nextVarNams = actModelerService.getNextVarNams(task.getTaskDefinitionKey(), processTaskDto.getDefId());
        String[] changFile = {};
        if (!StringUtils.isEmpty(nodesetEntity.getChangeFiles())) {
            changFile = nodesetEntity.getChangeFiles().split(",");
        }
        return Result.ok().put("changeFields", changFile).put("vars", nextVarNams);
    }

    /**
     * 办理任务时，获取下一个节点的信息
     *
     * @param processTaskDto
     * @return
     */
    @RequestMapping(value = "getNextActNodes", method = RequestMethod.POST)
    @ResponseBody
    public Result getNextActNodes(ProcessTaskDto processTaskDto) {
        List<ProcessNodeDto> nextActNodes = actModelerService.getNextActNodes(processTaskDto);
        return Result.ok().put("nextActNodes", nextActNodes);
    }

    /**
     * 转到审批任务选择下一级审批者页面
     *
     * @param processTaskDto
     * @param model
     * @return
     */
    @RequestMapping(value = "toDoActTaskView")
    public String toDoActTaskView(ProcessTaskDto processTaskDto, Model model) {
        Task task = taskService.createTaskQuery().taskId(processTaskDto.getTaskId()).singleResult();
        //查询需要作为流程条件判断的字段
        Set<String> nextVarNams = actModelerService.getNextVarNams(task.getTaskDefinitionKey(), processTaskDto.getDefId());
        //查询可更改字段
        ExtendActNodesetEntity nodesetEntity = nodesetService.queryByNodeId(task.getTaskDefinitionKey());
        //查询流程基本信息
        ExtendActFlowbusEntity flowbus = flowbusService.queryByBusIdInsId(processTaskDto.getInstanceId(), processTaskDto.getBusId());
        model.addAttribute("taskDto", processTaskDto);
        model.addAttribute("nodeSet", nodesetEntity);
        model.addAttribute("flowbus", flowbus);
        return "activiti/doActTask";
    }

    /**
     * 办理任务
     *
     * @param processTaskDto
     * @return
     */
    @RequestMapping(value = "doActTask", method = RequestMethod.POST)
    @ResponseBody
    public Result doActTask(ProcessTaskDto processTaskDto, HttpServletRequest request) {
        Result result = null;
        try {
            Map<String, String[]> parameterMap = request.getParameterMap();
            Map<String, Object> params = new LinkedCaseInsensitiveMap<>();
            for (String key : parameterMap.keySet()) {
                params.put(key, parameterMap.get(key)[0]);
            }

            // Collection<FlowElement> flowElements = jumpService.getActIdCollection(processTaskDto.getDefId());
            //jumpService.jumpEndActivity(processTaskDto.getDefId(),processTaskDto,processTaskDto.getInstanceId(),"sid-33BCE5B3-845D-412C-857F-DC24E63599D5");

            actModelerService.doActTask(processTaskDto, params);
            createPropertyNode(params);
            //why
//            taskService.complete(processTaskDto.getTaskId());
//            List<Task> tasks2 = taskService.createTaskQuery().processInstanceId(processTaskDto.getInstanceId()).list();
//            for(Task task : tasks2){
//                taskService.setAssignee(task.getId(), processTaskDto.getNextUserIds());
//            }
//            List<ProcessNodeDto> t = actModelerService.getNextActNodes(processTaskDto);
//            if(processTaskDto.getNextUserIds() == UserUtils.getCurrentUserId()){
//                int aaa = 1;
//            }
//            TaskQuery taskQuery = taskService.createTaskQuery();
//            taskQuery.taskAssignee(processTaskDto.getNextUserIds());
//            taskQuery.processDefinitionId(processTaskDto.getDefId());
//            List<Task> tasks =taskQuery.list();
//            for(Task task :tasks){
//                taskService.complete(task.getId());
//            }
            //跳过相同办案人
//            if(processTaskDto.getNextUserIds().equals(UserUtils.getCurrentUserId())){
//                List<Task> tasks2 = taskService.createTaskQuery().processInstanceId(processTaskDto.getInstanceId()).list();
//                for(Task task : tasks2){
//                    taskService.complete(task.getId());
//                }
//                List<Task> tasks3 = taskService.createTaskQuery().processInstanceId(processTaskDto.getInstanceId()).list();
//                for(Task task : tasks3){
//                    taskService.setAssignee(task.getId(), "a20a19e46419416c9ca4d46517258dae");
//                }
//            }

            result = Result.ok("办理任务成功");
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.error("办理任务失败");
        }

        createCaseNode(processTaskDto);
        return result;
    }


    /**
     * wxp
     * 添加属性结点
     *
     * @param processTaskDto
     */
    public void createCaseNode(ProcessTaskDto processTaskDto) {
        CaseDataNodeBean caseDataNodeBean = new CaseDataNodeBean();
        caseDataNodeBean.setCaseId(processTaskDto.getBusId());
        caseDataNodeBean.setNodeCreateUser(new CaseDataNodeBean.NodeUserBean(ShiroUtils.getUserEntity().getId(), ShiroUtils.getUserEntity().getUserName()));
        caseDataNodeBean.setCaseDetailUrl("null");
        caseDataNodeBean.setCaseName(processTaskDto.getTaskName());
        String curTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        caseDataNodeBean.setNodeCreateTime(curTime);
        caseDataNodeBean.setNodeName(processTaskDto.getTaskName());
        caseDataNodeBean.setRemark(processTaskDto.getRemark());
        caseDataNodeBean.setTaskId(processTaskDto.getTaskId());
        Neo4jFinalUtil.addCaseNode(caseDataNodeBean);
    }


    /**
     * wxp
     * 添加属性结点
     */
    public void createPropertyNode(Map<String, Object> params) {
        String caseId = params.get("busId").toString();

        for (String key : params.keySet()) {
            if (key.startsWith("prop_") || key.startsWith("file_") || key.startsWith("rule_")) {
                Map<String, Object> juv = new HashMap<>();
                juv.put("name", CaseEntity.kvMap.get(key) + " : " + params.get(key).toString());
                juv.put(CaseEntity.kvMap.get(key), params.get(key).toString());
                Neo4jFinalUtil.addKVs(caseId, key, "change", false, juv);
            }
        }

    }


    public void sendMessage(ProcessTaskDto processTaskDto) {

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
//        {
//            "label":"L",
//                "property":"name:'g'",
//                "type":"T2"
//        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("label", processTaskDto.getTaskId());
        jsonObject.put("property", processTaskDto.getBusId());
        jsonObject.put("type", processTaskDto.getInstanceId());
        RequestBody body = RequestBody.create(mediaType, jsonObject.toJSONString());
        Request request = new Request.Builder()
                .url("http://localhost:8081/save")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Cache-Control", "no-cache")
                .build();

        try {
            Response response = client.newCall(request).execute();

            log.debug(response.toString());
        } catch (Exception e) {

        }

    }

    /**
     * 驳回到任务发起人，重新编辑提交
     *
     * @param processTaskDto
     * @return
     */
    @RequestMapping(value = "backStartUser", method = RequestMethod.POST)
    @ResponseBody
    public Result backStartUser(ProcessTaskDto processTaskDto, HttpServletRequest request) {
        Result result = null;
        try {
            Map<String, String[]> parameterMap = request.getParameterMap();
            Map<String, Object> params = new LinkedCaseInsensitiveMap<>();
            for (String key : parameterMap.keySet()) {
                params.put(key, parameterMap.get(key)[0]);
            }
            actModelerService.endFailFolw(processTaskDto, params);
            result = Result.ok("驳回到发起人,成功");
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.error("驳回到发起人,失败");
        }
        return result;
    }

    /**
     * 转到转办页面
     *
     * @param processTaskDto
     * @param model
     * @return
     */
    @RequestMapping(value = "toTurnToDo")
    public String toTurnToDo(ProcessTaskDto processTaskDto, Model model) {
        //查询流程基本信息
        ExtendActFlowbusEntity flowbus = flowbusService.queryByBusIdInsId(processTaskDto.getInstanceId(), processTaskDto.getBusId());
        model.addAttribute("taskDto", processTaskDto);
        model.addAttribute("flowbus", flowbus);
        return "activiti/trunTask";
    }

    /**
     * 转办
     *
     * @param processTaskDto
     * @return
     */
    @RequestMapping(value = "turnToDo", method = RequestMethod.POST)
    @ResponseBody
    public Result turnToDo(ProcessTaskDto processTaskDto, String toUserId, HttpServletRequest request) {
        Result result;
        try {
            actModelerService.turnToDo(processTaskDto, toUserId);
            result = Result.ok("转办任务,成功");
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.error("转办任务,失败");
        }
        return result;
    }

    /**
     * 转到跳转节点选择页面(why)
     *
     * @param processTaskDto
     * @param model
     * @return
     */
    @RequestMapping(value = "toDoJump")
    public String toDoJump(ProcessTaskDto processTaskDto, Model model) {
        Task task = taskService.createTaskQuery().taskId(processTaskDto.getTaskId()).singleResult();
        //查询可更改字段
        ExtendActNodesetEntity nodesetEntity = nodesetService.queryByNodeId(task.getTaskDefinitionKey());
        //查询流程基本信息
        ExtendActFlowbusEntity flowbus = flowbusService.queryByBusIdInsId(processTaskDto.getInstanceId(), processTaskDto.getBusId());
        List<ActivityImpl> actList = jumpService.getActIdCollection(processTaskDto.getDefId());
        model.addAttribute("taskDto", processTaskDto);
        model.addAttribute("actList", actList);
        model.addAttribute("flowbus", flowbus);
        model.addAttribute("nodeSet", nodesetEntity);
        return "activiti/jumpSelect";
    }

    /**
     * 根据选择的跳转环节执行跳转(why)
     *
     * @param processTaskDto
     * @return
     */
    @RequestMapping(value = "doJump", method = RequestMethod.POST)
    @ResponseBody
    public Result doJump(ProcessTaskDto processTaskDto, HttpServletRequest request) {
        Result result = null;
        try {
            Map<String, String[]> parameterMap = request.getParameterMap();
            Map<String, Object> params = new LinkedCaseInsensitiveMap<>();
            for (String key : parameterMap.keySet()) {
                params.put(key, parameterMap.get(key)[0]);
            }
            String actId = (String) params.get("actId");
            jumpService.jumpEndActivity(processTaskDto.getDefId(), processTaskDto, processTaskDto.getInstanceId(), actId);
            result = Result.ok("任务跳转成功");
            createPropertyNode(params);
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.error("任务跳转失败");
        }
        createCaseNode(processTaskDto);
        return result;
    }
}
