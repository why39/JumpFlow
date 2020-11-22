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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    GJAJService gjajService;

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

    @RequestMapping(value="deletelog", method = RequestMethod.POST)
    @RequiresPermissions("act:model:all")
    @ResponseBody
    public Result deletelog(String bmsah) {
        return caseService.deleteLog(bmsah);
    }

    @RequestMapping(value = "ajlb")
    @ResponseBody
    public List<GJAJEntity> Ajlb(@RequestParam  String ajlb){
        /*try {
            ajlb = java.net.URLDecoder.decode(ajlb, "UTF-8");
            ajlb = new String(ajlb.getBytes("ISO-8859-1"),"UTF-8");
            System.out.println("ajlb+" +ajlb);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/
        List<GJAJEntity> gjajEntity = (List<GJAJEntity>) gjajService.queryObject2(ajlb);
        return gjajEntity;
    }
}
