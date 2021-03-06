package com.hxy.modules.demo.controller;

//import com.hxy.dq.Suggestion;
import com.hxy.modules.common.page.Page;
import com.hxy.modules.common.utils.CommUtils;
import com.hxy.modules.common.utils.Result;
import com.hxy.modules.common.utils.ShiroUtils;
import com.hxy.modules.common.utils.StringUtils;
import com.hxy.modules.demo.entity.CaseEntity;
import com.hxy.modules.demo.service.CaseService;
import com.hxy.provenance.logparser.*;
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
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 类的功能描述.
 * 业务树
 *
 * @Auther hxy
 * @Date 2017/7/25
 */
@RequestMapping("demo/case")
@Controller
public class CaseController {

    @Autowired
    CaseService caseService;

    @Autowired
    GJAJDao gjajDao;

    @Autowired
    GJRZDao gjrzDao;


    /**
     * 案件列表
     *
     * @param model
     * @param caseEntity
     * @param request
     * @return
     */
    @RequestMapping("list")
    @RequiresPermissions("act:model:all")
    public String list(Model model, CaseEntity caseEntity, HttpServletRequest request) {
        int pageNum = CommUtils.parseInt(request.getParameter("pageNum"), 1);
        Page<CaseEntity> page = caseService.findPage(caseEntity, pageNum);
        model.addAttribute("page", page);
        model.addAttribute("case", caseEntity);
        return "demo/case";
    }

    /**
     * 案件质量
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping("dataquality")
    @RequiresPermissions("act:model:all")
    public String dataquality(Model model, HttpServletRequest request) {
        return "demo/dataquality_tb";
    }

    List<DqResEntity> dqResEntityList = new ArrayList<>();

//    @RequestMapping(value = "dealquality", method = RequestMethod.POST)
//    @RequiresPermissions("act:model:all")
//    @ResponseBody
//    public Result dealquality(Model model, String tb_name, HttpServletRequest request) {
//        Map<String, Object> result = null;
//        dqResEntityList.clear();
//
//        if ("tb_dq_fzxyr".equals(tb_name)) {
//            result = Suggestion.dealXYR("case/" + UUID.randomUUID().toString());
//        } else {
//            result = Suggestion.dealAKX("case/" + UUID.randomUUID().toString());
//        }
//
//        Set<Map.Entry<String, Object>> entrys = result.entrySet();
//        for (Map.Entry<String, Object> entry : entrys) {
//            if (!"code".equals(entry.getKey())) {
//                dqResEntityList.add(new DqResEntity(entry.getKey(), entry.getValue().toString()));
//            }
//        }
//
//        if (dqResEntityList.size() > 0) {
//            model.addAttribute("dq_res", dqResEntityList);
//        } else {
//            model.addAttribute("dq_res", null);
//        }
//        return Result.ok(result);
//    }

    @RequestMapping("dataquality_res")
    @RequiresPermissions("act:model:all")
    public String dataqualityRes(Model model, HttpServletRequest request) {
        if (dqResEntityList.size() > 0) {
            model.addAttribute("dq_res", dqResEntityList);
        } else {
            model.addAttribute("dq_res", null);
        }
        return "demo/dataquality_res";
    }

    /**
     * 请假详情
     *
     * @param model
     * @param id
     * @param request
     * @return
     */
    @RequestMapping("info")
    @RequiresPermissions("act:model:all")
    public String info(Model model, String id, HttpServletRequest request) {
        if (!StringUtils.isEmpty(id)) {
            CaseEntity caseEntity = caseService.queryObject(id);
            model.addAttribute("case", caseEntity);
        }
        return "demo/caseEdit";
    }

    /**
     * 编辑
     *
     * @param caseEntity
     * @return
     */
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @RequiresPermissions("act:model:all")
    @ResponseBody
    public Result edit(CaseEntity caseEntity) {
        System.out.println("wxp>>>>>>>>>>>> : edit");
        if (StringUtils.isEmpty(caseEntity.getId())) {
            caseService.save(caseEntity);
            addCase(caseEntity);
        } else {
            caseService.update(caseEntity);
        }
        return Result.ok();
    }

    /**
     * wxp
     * 添加案件结点
     *
     * @param caseEntity
     */
    public void addCase(CaseEntity caseEntity) {
        GJAJEntity caseDataBean = new GJAJEntity();
        caseDataBean.setAJLB_MC("测试分类");
        caseDataBean.setBMSAH(caseEntity.getId());
        caseDataBean.setAJMC(caseEntity.getTitle());
        caseDataBean.setCJSJ(java.util.Date.from(Instant.now()));

//        caseDataBean.setDepartment_name("监督办");
//        caseDataBean.setUser_id(ShiroUtils.getUserEntity().getId());
//        caseDataBean.setUser_name(ShiroUtils.getUserEntity().getUserName());

//        GJNeo4jUtil.addCase(caseDataBean);
        gjajDao.save(caseDataBean);

        GJRZEntity gjrzEntity = new GJRZEntity();
        gjrzEntity.setID(UUID.randomUUID().toString());
        gjrzEntity.setBMSAH(caseEntity.getId());
        gjrzEntity.setCZRM(ShiroUtils.getUserEntity().getUserName());
        gjrzEntity.setRZMS("创建案件");
        gjrzEntity.setEJFL("property_head");
        gjrzEntity.setZHXGSJ(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date().getTime()));
        gjrzDao.save(gjrzEntity);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @RequiresPermissions("act:model:all")
    @ResponseBody
    public Result edit(String id) {
        if (caseService.delete(id) < 1) {
            return Result.error("删除失败");
        }
        return Result.ok("删除成功");
    }


    @Autowired
    private Environment env;

    @RequestMapping(value = "neoconfig", method = RequestMethod.GET)
    @ResponseBody
    public String getNeoConfig() {
        String NEO_SERVER_URL = env.getProperty("spring.data.neo4j.uri");
        String NEO_SERVER_USER = env.getProperty("spring.data.neo4j.username");
        String NEO_SERVER_PSW = env.getProperty("spring.data.neo4j.password");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("NEO_SERVER_URL", NEO_SERVER_URL);
        jsonObject.put("NEO_SERVER_USER", NEO_SERVER_USER);
        jsonObject.put("NEO_SERVER_PSW", NEO_SERVER_PSW);

        return jsonObject.toString();

    }


}
