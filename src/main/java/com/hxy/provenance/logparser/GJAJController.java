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
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

/**
 * 对接高检数据库用
 */
@RequestMapping("demo/gj")
@Controller
@Scope(value = "prototype")
public class GJAJController {

    @Autowired
    GJAJService caseService;

    @Autowired
    DataPlatformService dataPlatformService;

    @Autowired
    GJAJService gjajService;

    @RequestMapping("gjajlb")
    @RequiresPermissions("act:model:all")
    public String gjajlb(Model model, GJAJEntity caseEntity, HttpServletRequest request) {
        int pageNum = CommUtils.parseInt(request.getParameter("pageNum"), 1);
        Page<GJAJEntity> page = caseService.findPage(caseEntity, pageNum);
        model.addAttribute("page", page);
        model.addAttribute("case", caseEntity);
        return "demo/gjajlb";
    }

    @RequestMapping(value = "parselog", method = RequestMethod.POST)
    @RequiresPermissions("act:model:all")
    @ResponseBody
    public Result parselog(String bmsah) {
        //解析某个案件的所有日志
        return caseService.parseLog(bmsah);
    }

    @RequestMapping(value = "parselog-test", method = RequestMethod.POST)
    @RequiresPermissions("act:model:all")
    @ResponseBody
    public Result parselogForTest(int count) {
        //解析某个案件的所有日志
        return caseService.parseLogForTest(count);
    }

    @RequestMapping(value = "delete-test", method = RequestMethod.POST)
    @RequiresPermissions("act:model:all")
    @ResponseBody
    public Result deleteTest(int count) {
        //解析某个案件的所有日志
        return caseService.deleteTest(count);
    }

    @RequestMapping(value = "deletelog", method = RequestMethod.POST)
    @RequiresPermissions("act:model:all")
    @ResponseBody
    public Result deletelog(String bmsah) {
        return caseService.deleteLog(bmsah);
    }

    @RequestMapping(value = "ajlb")
    @ResponseBody
    public List<GJAJEntity> Ajlb(@RequestParam String ajlb) {
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

    @RequestMapping(value = "count-ajlb")
    @ResponseBody
    public List<Map<String, Object>> countAJLB() {
        return gjajService.countAJLB();
    }

    @RequestMapping(value = "count-type-fields")
    @ResponseBody
    public List<Map<String, Object>> countAJLBField(@RequestParam String ajlb, @RequestParam int size) {
        if(ajlb.equals("xsjc")){
            ajlb = "刑事检察";
        }
        if(ajlb.equals("xszx")){
            ajlb = "刑事执行";
        }
        if(ajlb.equals("gyss")){
            ajlb = "公益诉讼";
        }
        if(ajlb.equals("ms")){
            ajlb = "民事";
        }
        if(ajlb.equals("xz")){
            ajlb = "行政";
        }
        if(ajlb.equals("jwb")){
            ajlb = "检委办";
        }
        if(ajlb.equals("wjyw")){
            ajlb = "未检业务";
        } if(ajlb.equals("kgss")){
            ajlb = "控告申诉";
        } if(ajlb.equals("dtyw")){
            ajlb = "对台业务";
        } if(ajlb.equals("ag")){
            ajlb = "案管";
        } if(ajlb.equals("sfxz")){
            ajlb = "司法协助";
        }
        List<Map<String, Object>> map = new ArrayList<>();
        List<Map<String, Object>> list = caseService.countFields(ajlb, size);
        for(Map<String, Object> item:list){
            if(KVCache.kv.containsKey(item.get("field").toString().toUpperCase())){
                Map<String, Object> hash = new HashMap<>();
                hash.put("field",KVCache.kv.get(item.get("field").toString().toUpperCase()).cn);
                hash.put("count_man",item.get("count_man"));
                map.add(hash);
            }

        }
        return map;
    }
}
