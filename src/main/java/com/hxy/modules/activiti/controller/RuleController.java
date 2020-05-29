package com.hxy.modules.activiti.controller;

import com.hxy.modules.activiti.dto.ProcessTaskDto;
import com.hxy.modules.activiti.entity.RuleEntity;
import com.hxy.modules.activiti.service.JumpService;
import com.hxy.modules.activiti.service.RuleService;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Controller
public class RuleController {
    @Autowired
    RuleService ruleService;
    @Autowired
    JumpService jumpService;
    /**
     *  规则引导页
     */
    @RequestMapping(value="/ruleIndex")
    public String ruleIndex(ProcessTaskDto processTaskDto, Model model){
//        RuleEntity rule = new RuleEntity();
//        rule.setId("4");
//        rule.setModelId("33");
//        rule.setDeploymentId("333");
//        rule.setExpression("4444");
//        rule.setStartEvent("33333");
//        rule.setEndEvent("444444");
//        ruleService.save(rule);
        List<RuleEntity> lr = new ArrayList<>();
        lr = ruleService.queryRulesAva("33","333","33333");

        List<ActivityImpl> actList = jumpService.getActIdCollection(processTaskDto.getDefId());
        //扫描文书模板
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
        model.addAttribute("files",files);
        model.addAttribute("taskDto",processTaskDto);
        model.addAttribute("actList",actList);
        return "rule/ruleIndex";
    }

    /**
     *  查看已存在的规则
     */
    @RequestMapping(value="/ruleExt")
    public String ruleExt(ProcessTaskDto processTaskDto, Model model){
        List<RuleEntity> lr = new ArrayList<>();
        //lr = ruleService.queryRulesAll(processTaskDto.getDefId(),processTaskDto.getInstanceId());
        lr = ruleService.queryRulesAll("100","10100");
        model.addAttribute("ruleList", lr);
        return "rule/ruleExt";
    }
}
