package com.hxy.provenance.logparser;

import com.hxy.modules.common.page.Page;
import com.hxy.modules.common.utils.CommUtils;
import com.hxy.modules.common.utils.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 对接数据汇聚平台用
 */
@RequestMapping("demo/platform")
@RestController
public class DataPlatformController {

    @Autowired
    GJAJService caseService;

    @Autowired
    DataPlatformService dataPlatformService;

    @RequestMapping("gjajlb")
    @ResponseBody
    public List<GJAJEntity> platformAjlb(String start, String end) {
        return dataPlatformService.queryAJ(start, end);
    }

    @RequestMapping("gjrzlb")
    @ResponseBody
    public List<GJRZEntity> platformRzlb(String start, String end) {
        return dataPlatformService.queryRZ(start, end);
    }

}
