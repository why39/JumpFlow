package com.hxy.provenance.staticcompile;

import com.hxy.modules.common.utils.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("static/staticCompile")
@Controller
@Scope(value = "prototype")
public class staticInfoController {

    @Autowired staticInfoService staticinfoservice;
    @RequestMapping(value = "staticGen", method = RequestMethod.POST)
    @RequiresPermissions("act:model:all")
    @ResponseBody
    public Result staticGen(String category,String bmsah) {
        //解析某个案件的所有日志
        return staticinfoservice.insertNode(category,bmsah);
    }

}
