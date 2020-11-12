package com.hxy.provenance.logparser;

import com.hxy.modules.common.page.Page;
import com.hxy.modules.common.utils.CommUtils;
import com.hxy.modules.common.utils.Result;
import com.hxy.modules.common.utils.ShiroUtils;
import com.hxy.modules.common.utils.StringUtils;
import com.hxy.modules.demo.entity.CaseEntity;
import com.hxy.modules.demo.service.CaseService;
import com.hxy.provenance.neo4j.CaseDataBean;
import com.hxy.provenance.neo4j.Neo4jFinalUtil;
import com.hxy.provenance.neo4j.json.JSONObject;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 对接高检数据库用
 */
@RequestMapping("demo/gj")
@Controller
public class GJAJController {

    @Autowired
    GJAJService caseService;

    @Autowired
    DataPlatformService dataPlatformService;

    @RequestMapping("gjajlb")
    @RequiresPermissions("act:model:all")
    public String gjajlb(Model model , GJAJEntity caseEntity, HttpServletRequest request){
        int pageNum = CommUtils.parseInt(request.getParameter("pageNum"), 1);
        Page<GJAJEntity> page = caseService.findPage(caseEntity, pageNum);
        model.addAttribute("page",page);
        model.addAttribute("case",caseEntity);

//        dataPlatformService.queryAJ("2019-11-08 15:10:42", "2019-11-08 23:10:42");

        return "demo/gjajlb";
    }

    @RequestMapping(value="parselog", method = RequestMethod.POST)
    @RequiresPermissions("act:model:all")
    @ResponseBody
    public Result parselog(String bmsah){
        //解析某个案件的所有日志
        return caseService.parseLog(bmsah);
    }





}
