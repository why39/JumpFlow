package com.hxy.modules.activiti.controller;

import com.hxy.modules.activiti.dto.ProcessTaskDto;
import com.hxy.modules.activiti.entity.ExtendActNodesetEntity;
import com.hxy.modules.activiti.entity.RuleEntity;
import com.hxy.modules.activiti.service.ActModelerService;
import com.hxy.modules.activiti.service.ExtendActNodesetService;
import com.hxy.modules.activiti.service.JumpService;
import com.hxy.modules.activiti.service.RuleService;
import com.hxy.modules.activiti.utils.ActUtils;
import com.hxy.modules.common.annotation.SysLog;
import com.hxy.modules.common.utils.Result;
import com.hxy.modules.common.utils.ShiroUtils;
import com.hxy.modules.common.utils.UserUtils;
import com.hxy.modules.demo.entity.CaseEntity;
import com.hxy.provenance.neo4j.CaseDataNodeBean;
import com.hxy.provenance.neo4j.Neo4jFinalUtil;
import com.zhuozhengsoft.pageoffice.DocumentVersion;
import com.zhuozhengsoft.pageoffice.FileSaver;
import com.zhuozhengsoft.pageoffice.OpenModeType;
import com.zhuozhengsoft.pageoffice.PageOfficeCtrl;
import com.zhuozhengsoft.pageoffice.wordreader.WordDocument;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedCaseInsensitiveMap;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class Thymetest {
    @Autowired
    RuleService ruleService;
    @Autowired
    JumpService jumpService;
    @Autowired
    ActModelerService actModelerService;
    @Autowired
    TaskService taskService;
    @Autowired
    ExtendActNodesetService nodesetService;
    @Value("${pageoffice.posyspath}")
    private String poSysPath;
    @Value("${pageoffice.popassword}")
    private String poPassWord;

    /**

     * 添加PageOffice的服务器端授权程序Servlet（必须）

     * @return

     */

    @Bean

    public ServletRegistrationBean servletRegistrationBean() {

        com.zhuozhengsoft.pageoffice.poserver.Server poserver = new com.zhuozhengsoft.pageoffice.poserver.Server();

        //设置PageOffice注册成功后,license.lic文件存放的目录

        poserver.setSysPath(poSysPath);

        ServletRegistrationBean srb = new ServletRegistrationBean(poserver);

        srb.addUrlMappings("/poserver.zz");

        srb.addUrlMappings("/posetup.exe");

        srb.addUrlMappings("/pageoffice.js");

        srb.addUrlMappings("/sealsetup.exe");

        return srb;

    }

    /**

     * 添加印章管理程序Servlet（可选）

     * @return

     */

    @Bean

    public ServletRegistrationBean servletRegistrationBean2() {

        com.zhuozhengsoft.pageoffice.poserver.AdminSeal adminSeal = new com.zhuozhengsoft.pageoffice.poserver.AdminSeal();

        adminSeal.setAdminPassword(poPassWord);//设置印章管理员admin的登录密码

        //设置印章数据库文件poseal.db存放目录，该文件在当前demo的“集成文件”夹中

        adminSeal.setSysPath(poSysPath);



        ServletRegistrationBean srb = new ServletRegistrationBean(adminSeal);

        srb.addUrlMappings("/adminseal.zz");

        srb.addUrlMappings("/sealimage.zz");

        srb.addUrlMappings("/loginseal.zz");

        return srb;

    }

    /**

     * 添加PageOffice的服务器端授权程序Servlet（必须）,第一个配置bean无效

     * @return

     */

    @Bean

    public ServletRegistrationBean servletRegistrationBean3() {

        com.zhuozhengsoft.pageoffice.poserver.Server ps = new com.zhuozhengsoft.pageoffice.poserver.Server();


        ps.setSysPath(poSysPath);



        ServletRegistrationBean srb = new ServletRegistrationBean(ps);

        srb.addUrlMappings("/plib/poserver.zz");

        srb.addUrlMappings("/plib/posetup.exe");

        srb.addUrlMappings("/plib/pageoffice.js");

        srb.addUrlMappings("/plib/sealsetup.exe");

        return srb;

    }

    /**

     * 打开已有文书模板

     * @return

     */

    @RequestMapping(value="/word", method= RequestMethod.GET)
    public String showWord(String fileName, HttpServletRequest request, HttpServletResponse response){
        String fn = fileName;
        PageOfficeCtrl poCtrl=new PageOfficeCtrl(request);

        request.setAttribute("poCtrl", poCtrl);

        //设置服务页面
        poCtrl.setServerPage(request.getContextPath()+"/plib/poserver.zz");
        //添加保存按钮
        poCtrl.addCustomToolButton("保存并关闭","Save",1);
        //设置保存的action
        poCtrl.setSaveFilePage("savefile");
        //打开word
        //poCtrl.webOpen("d:\\test.wps",OpenModeType.docAdmin,"张三");
        poCtrl.webOpen("doc/"+fn,OpenModeType.docAdmin,"张三");   //这里相对路径是读取target/doc下的文件，而不是src/doc下的文件
        poCtrl.setTagId("PageOfficeCtrl1"); //此行必须
        //--- PageOffice的调用代码 结束 -----


        return "pageoffice/word";

    }

    /**

     * 保存已有文档模板的修改（更新已有模板）

     * @return

     */
    @RequestMapping(value = "/savefile")

    public void saveFile(HttpServletRequest request, HttpServletResponse response){

        FileSaver fs = new FileSaver(request, response);

        //fs.saveToFile("d:\\" + fs.getFileName());
        try {
            fs.saveToFile(ResourceUtils.getURL("classpath:").getPath() + "/statics/doc/instances/" + fs.getFileName());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        fs.close();
    }

    /**

     * 创建新文书模板

     * @return

     */

    @RequestMapping(value="/createword", method= RequestMethod.GET)
    public String createWord(String fileName, HttpServletRequest request, HttpServletResponse response){
        String fn = fileName;
        PageOfficeCtrl poCtrl=new PageOfficeCtrl(request);

        request.setAttribute("poCtrl", poCtrl);

        //设置服务页面
        poCtrl.setServerPage(request.getContextPath()+"/plib/poserver.zz");
        //添加保存按钮
        poCtrl.addCustomToolButton("保存并关闭","Save",1);
        //设置保存的action
        poCtrl.setSaveFilePage("saveCreateFile?fileName="+fn);
        //创建word
        poCtrl.webCreateNew("张三", DocumentVersion.Word2007);
        poCtrl.setFileTitle(fn);
        poCtrl.setTagId("PageOfficeCtrl1"); //此行必须
        //--- PageOffice的调用代码 结束 -----


        return "pageoffice/word";

    }

    /**

     * 保存新建的文档模板

     * @return

     */
    @RequestMapping(value = "/saveCreateFile")

    public void saveCreateFile(String fileName, HttpServletRequest request, HttpServletResponse response){
        String fn = fileName;
        FileSaver fs = new FileSaver(request, response);
        //fs.saveToFile("d:\\" + fs.getFileName());
        try {
            fs.saveToFile(ResourceUtils.getURL("classpath:").getPath() + "/statics/doc/" + fs.getFileName());
            File file = new File(ResourceUtils.getURL("classpath:").getPath() + "/statics/doc/" + fs.getFileName());
            //将文书模板名称从默认的newword.docx改为自定义的名称
            file.renameTo(new File(ResourceUtils.getURL("classpath:").getPath() + "/statics/doc/" + fn + ".docx"));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        fs.close();
    }

    /**

     * 打开已有文书模板实例

     * @return

     */

    @RequestMapping(value="/wordIns", method= RequestMethod.GET)
    public String showWordInstance(String fileName, HttpServletRequest request, HttpServletResponse response){
        String fn = fileName;
        PageOfficeCtrl poCtrl=new PageOfficeCtrl(request);

        request.setAttribute("poCtrl", poCtrl);

        //设置服务页面
        poCtrl.setServerPage(request.getContextPath()+"/plib/poserver.zz");
        //添加保存按钮
        poCtrl.addCustomToolButton("保存并关闭","Save",1);
        //设置保存的action
        poCtrl.setSaveFilePage("savefile");
        //打开word
        //poCtrl.webOpen("d:\\test.wps",OpenModeType.docAdmin,"张三");
        poCtrl.webOpen("doc/instances/"+fn,OpenModeType.docAdmin,"张三");   //这里相对路径是读取target/doc下的文件，而不是src/doc下的文件
        poCtrl.setTagId("PageOfficeCtrl1"); //此行必须
        //--- PageOffice的调用代码 结束 -----


        return "pageoffice/word";

    }

    /**

     * 引导页

     * @return

     */

    @RequestMapping(value="/pageIndex", method= RequestMethod.GET)
    public String pageIndex(ProcessTaskDto processTaskDto, HttpServletRequest request, Model model){
        //扫描文书存储目录下的所有文书文件
        String path = null;
        try{
            path = ResourceUtils.getURL("classpath:").getPath() + "/statics/doc/";
        }
        catch (Exception e){
            e.printStackTrace();
        }
        ArrayList<String> files = new ArrayList<String>();
        File file = new File(path);
        File[] tempList = file.listFiles();
        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
                files.add(tempList[i].getName());
            }
        }

        Task task = taskService.createTaskQuery().taskId(processTaskDto.getTaskId()).singleResult();
        //查询可更改字段
        ExtendActNodesetEntity nodesetEntity = nodesetService.queryByNodeId(task.getTaskDefinitionKey());
        model.addAttribute("files",files);
        model.addAttribute("taskDto", processTaskDto);
        model.addAttribute("nodeSet", nodesetEntity);
        return "pageoffice/filebookIndex";
    }
    /**
     *  提交文书
     */
    @RequestMapping(value="/submitFile", method = RequestMethod.POST)
    @ResponseBody
    public Result submitFile(javax.servlet.http.HttpServletRequest request, Model model){
        Result result = null;
        try{
            Map<String, String[]> parameterMap = request.getParameterMap();
            Map<String, Object> params = new LinkedCaseInsensitiveMap<>();
            for (String key : parameterMap.keySet()) {
                params.put(key, parameterMap.get(key)[0]);
            }
            ProcessTaskDto processTaskDto = new ProcessTaskDto();
            processTaskDto.setBusId((String)params.get("busId"));
            processTaskDto.setTaskId((String)params.get("taskId"));
            processTaskDto.setTaskName((String)params.get("taskName"));
            processTaskDto.setInstanceId((String)params.get("instanceId"));
            processTaskDto.setDefId((String)params.get("defId"));
            String expression = (String)params.get("expression");
            String actId = ActUtils.findActivitiImpl(processTaskDto.getTaskId(),"").getId();
            List<RuleEntity> lr = ruleService.queryRulesAva(processTaskDto.getDefId(), processTaskDto.getInstanceId(),actId);
            String nextAct = "";
            String nextActName = "";
            String rId = "";
            for(RuleEntity re : lr){
                if(ruleService.compareRule(re.getExpression(), expression) == true){
                    nextAct = re.getEndEvent();
                    nextActName = re.getEndName();
                    rId = re.getId();
                    break;
                }
            }
            if(nextAct != ""){
                result = Result.ok("根据规则"+rId+",跳转到"+nextActName);
                jumpService.jumpEndActivity(processTaskDto.getDefId(),processTaskDto,processTaskDto.getInstanceId(),nextAct);

            }
            else{
                result = Result.ok("没有相应规则，将按流程定义执行流程");
                actModelerService.doActTask(processTaskDto, params);
            }
            createPropertyNode(params);
            createCaseNode(processTaskDto);
        }
        catch (Exception e){
            e.printStackTrace();
            result = Result.error("提交文书并执行流程失败");
        }
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

}
