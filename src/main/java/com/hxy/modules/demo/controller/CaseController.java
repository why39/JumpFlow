package com.hxy.modules.demo.controller;

import com.hxy.modules.common.page.Page;
import com.hxy.modules.common.utils.CommUtils;
import com.hxy.modules.common.utils.Result;
import com.hxy.modules.common.utils.StringUtils;
import com.hxy.modules.demo.entity.CaseEntity;
import com.hxy.modules.demo.service.CaseService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 类的功能描述.
 * 业务树
 * @Auther hxy
 * @Date 2017/7/25
 */
@RequestMapping("demo/case")
@Controller
public class CaseController {

    @Autowired
    CaseService caseService;

    /**
     * 请假列表
     * @param model
     * @param caseEntity
     * @param request
     * @return
     */
    @RequestMapping("list")
    @RequiresPermissions("act:model:all")
    public String list(Model model , CaseEntity caseEntity, HttpServletRequest request){
        int pageNum = CommUtils.parseInt(request.getParameter("pageNum"), 1);
        Page<CaseEntity> page = caseService.findPage(caseEntity, pageNum);
        model.addAttribute("page",page);
        model.addAttribute("case",caseEntity);
        return "demo/case";
    }

    /**
     * 请假详情
     * @param model
     * @param id
     * @param request
     * @return
     */
    @RequestMapping("info")
    @RequiresPermissions("act:model:all")
    public String info(Model model , String id, HttpServletRequest request){
        if(!StringUtils.isEmpty(id)){
            CaseEntity caseEntity = caseService.queryObject(id);
            model.addAttribute("case",caseEntity);
        }
        return "demo/caseEdit";
    }

    /**
     * 编辑
     * @param caseEntity
     * @return
     */
    @RequestMapping(value = "edit",method = RequestMethod.POST)
    @RequiresPermissions("act:model:all")
    @ResponseBody
    public Result edit(CaseEntity caseEntity){
        if(StringUtils.isEmpty(caseEntity.getId())){
            caseService.save(caseEntity);
        }else {
            caseService.update(caseEntity);
        }
        return Result.ok();
    }


    /**
     * 删除
     * @param id
     * @return
     */
    @RequestMapping(value = "delete",method = RequestMethod.POST)
    @RequiresPermissions("act:model:all")
    @ResponseBody
    public Result edit(String id){
       if(caseService.delete(id)<1){
           return Result.error("删除失败");
       }
        return Result.ok("删除成功");
    }








}
