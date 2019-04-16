package com.hxy.modules.demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hxy.modules.activiti.dto.ProcessTaskDto;
import com.hxy.modules.common.utils.StringUtils;
import com.hxy.modules.demo.entity.CaseEntity;
import com.hxy.modules.demo.entity.LeaveEntity;
import com.hxy.modules.demo.service.CaseService;
import com.hxy.modules.demo.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 类的功能描述.
 * 流程相关的业务根据业务id查询公共类，路径为actKey，也就是业务key
 *
 * @Auther hxy
 * @Date 2017/8/7
 */
@RequestMapping("act/busInfo")
@Controller
public class ActBusInfoController {

    @Autowired
    CaseService caseService;

    @RequestMapping(value = "case", method = RequestMethod.POST)
    public String leave(ProcessTaskDto processTaskDto, Model model, String flag) {
        CaseEntity caseEntity = caseService.queryObject(processTaskDto.getBusId());
        model.addAttribute("caseEntity", caseEntity);
        model.addAttribute("taskDto", processTaskDto);
        model.addAttribute("flag", flag);

        //wxp:添加动态字段的值。
        if (!StringUtils.isEmpty(caseEntity.getFields())) {
            JSONObject map = JSON.parseObject(caseEntity.getFields());
            for (String key : map.keySet()) {
                System.out.println("wxp>>>>>>>>>>>> : " + key + " | " + map.get(key));
                model.addAttribute(key, map.get(key));
            }
        }
        return "/demo/caseActInfo";
    }
}
