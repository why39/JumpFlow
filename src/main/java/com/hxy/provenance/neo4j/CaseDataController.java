package com.hxy.provenance.neo4j;


import com.hxy.provenance.neo4j.entity.AjaxResult;
import com.hxy.provenance.neo4j.json.JSONObject;
import com.hxy.provenance.neo4j.utils.ServletUtils;
import com.hxy.provenance.neo4j.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

import static com.hxy.provenance.neo4j.NeoConstants.KEY_CASE_HEAD_ID;
import static com.hxy.provenance.neo4j.NeoConstants.KEY_CASE_NEW_NODE_ID;


/**
 * 数据世系
 * http://localhost:8083/swagger-ui.html
 *
 * @author ruoyi
 */
@Api("案件数据管理")
@Controller
@RequestMapping("/demo/data")
public class CaseDataController {
    private Logger logger = LoggerFactory.getLogger(CaseDataController.class);
    private String prefix = "demo/data";

//    /**
//     * 设置请求分页数据
//     */
//    protected void startPage()
//    {
//        PageDomain pageDomain = TableSupport.buildPageRequest();
//        Integer pageNum = pageDomain.getPageNum();
//        Integer pageSize = pageDomain.getPageSize();
//        if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize))
//        {
//            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
//            PageHelper.startPage(pageNum, pageSize, orderBy);
//        }
//    }

    /**
     * 获取request
     */
    public HttpServletRequest getRequest()
    {
        return ServletUtils.getRequest();
    }

    /**
     * 获取response
     */
    public HttpServletResponse getResponse()
    {
        return ServletUtils.getResponse();
    }

    /**
     * 获取session
     */
    public HttpSession getSession()
    {
        return getRequest().getSession();
    }

//    /**
//     * 响应请求分页数据
//     */
//    @SuppressWarnings({ "rawtypes", "unchecked" })
//    protected TableDataInfo getDataTable(List<?> list)
//    {
//        TableDataInfo rspData = new TableDataInfo();
//        rspData.setCode(0);
//        rspData.setRows(list);
//        rspData.setTotal(new PageInfo(list).getTotal());
//        return rspData;
//    }

    /**
     * 响应返回结果
     *
     * @param rows 影响行数
     * @return 操作结果
     */
    protected AjaxResult toAjax(int rows)
    {
        return rows > 0 ? success() : error();
    }

    /**
     * 响应返回结果
     *
     * @param result 结果
     * @return 操作结果
     */
    protected AjaxResult toAjax(boolean result)
    {
        return result ? success() : error();
    }

    /**
     * 返回成功
     */
    public AjaxResult success()
    {
        return AjaxResult.success();
    }

    /**
     * 返回失败消息
     */
    public AjaxResult error()
    {
        return AjaxResult.error();
    }

    /**
     * 返回成功消息
     */
    public AjaxResult success(String message)
    {
        return AjaxResult.success(message);
    }

    /**
     * 返回失败消息
     */
    public AjaxResult error(String message)
    {
        return AjaxResult.error(message);
    }

    /**
     * 返回错误码消息
     */
    public AjaxResult error(AjaxResult.Type type, String message)
    {
        return new AjaxResult(type, message);
    }

    /**
     * 页面跳转
     */
    public String redirect(String url)
    {
        return StringUtils.format("redirect:{}", url);
    }

    @Autowired
    ICaseDataService caseDataService;

    /**
     * 插入案件数据
     */
    @ApiOperation("插入新案件数据")
    @PostMapping("/addcase")
    @ResponseBody
    public AjaxResult addCase(CaseDataBean caseDataBean) {
        return toAjax(caseDataService.addCase(caseDataBean));
    }

//    @GetMapping("/listpage")
//    public String listPage(CaseDataBean caseDataBean) {
//        return prefix + "/listpage";
//    }
//
//    @GetMapping("/neopage")
//    public String neoPage(CaseDataBean caseDataBean) {
//        return prefix + "/neopage";
//    }


//    /**
//     * 案件汇总
//     *
//     * @return
//     */
//    @ApiOperation("案件汇总表格汇总")
//    @PostMapping("/listtable")
//    @ResponseBody
//    public TableDataInfo listTable(CaseDataBean caseDataBean) {
//        logger.debug("listData>>>>>" + caseDataBean.getCase_name());
//        startPage();
//        List<CaseDataBean> list = caseDataService.queryCaseList(caseDataBean);
//        logger.debug(list.size() + "<<<<<");
//        return getDataTable(list);
//    }

//    @ApiOperation("案件汇总")
//    @PostMapping("/listdata")
//    @ResponseBody
//    public List<CaseDataBean> listData(CaseDataBean caseDataBean) {
//        startPage();
//        List<CaseDataBean> list = caseDataService.queryCaseList(caseDataBean);
//        return list;
//    }

    @ApiOperation("添加案件节点")
    @PostMapping("/addnode")
    @ResponseBody
    public String addNode(CaseDataNodeBean caseDataNodeBean) {
        String caseId = caseDataNodeBean.getCaseId();
        JSONObject caseProvData = caseDataService.queryCaseProvData(caseId);
        if (caseProvData != null) {
            String lastNodeId = caseProvData.getStr(KEY_CASE_NEW_NODE_ID);
            String nodeId = Neo4jFinalUtil.createNode(caseDataNodeBean, lastNodeId);
            caseProvData.put(KEY_CASE_NEW_NODE_ID, nodeId);
            caseDataService.updateCaseProvData(caseDataNodeBean.getCaseId(), caseProvData);
            return nodeId;
        } else {
            return "-1";
        }
    }

    /**
     * 添加自定义节点
     *
     * @param label    新增节点的label
     * @param relation 节点之间关系名称
     * @param reverse  true:this->last, false:last->this
     * @param map      节点属性键值对
     * @return
     */
    @ApiOperation("添加自定义节点")
    @PostMapping("/addkv")
    @ResponseBody
    public String addKVs(String caseId, String label, String relation, boolean reverse, @RequestBody Map<String, Object> map) {
        JSONObject caseProvData = caseDataService.queryCaseProvData(caseId);
        if (caseProvData != null) {
            String lastNodeId = null;
            if (map != null && map.get("lastNodeId") != null) {
                lastNodeId = (String) map.get("lastNodeId");
            } else {
                lastNodeId = caseProvData.getStr(label);//如果有相同标签的最新节点id则作为上一个节点id
            }

            if (lastNodeId == null || "".equals(lastNodeId)) {
                lastNodeId = caseProvData.getStr(KEY_CASE_HEAD_ID);//否则以案件虚拟头部节点作为前一个节点
                map.put("lastNodeId", lastNodeId);
            }

            String nodeId = Neo4jFinalUtil.createKeyValues(caseId, label, lastNodeId, relation, reverse, map);
            caseProvData.put(label, nodeId);
            caseDataService.updateCaseProvData(caseId, caseProvData);
            return nodeId;
        } else {
            return "-1";
        }
    }



}
