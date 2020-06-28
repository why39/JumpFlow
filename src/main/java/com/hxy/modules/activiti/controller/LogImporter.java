package com.hxy.modules.activiti.controller;

import com.hxy.modules.common.utils.Result;
import com.hxy.provenance.log.caseLogBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedCaseInsensitiveMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class LogImporter {
    /**
     *  导入日志
     */
    @RequestMapping(value="/logImport", method = RequestMethod.POST)
    @ResponseBody
    public Result logImport(javax.servlet.http.HttpServletRequest request, Model model){
        Result result = null;
        try{
            Map<String, String[]> parameterMap = request.getParameterMap();
            Map<String, Object> params = new LinkedCaseInsensitiveMap<>();
            for (String key : parameterMap.keySet()) {
                params.put(key, parameterMap.get(key)[0]);
            }
            String caseId = (String)params.get("caseId");
            caseLogBuilder.caseLogBuild(caseId);
        }
        catch (Exception e){
            e.printStackTrace();
            result = Result.error("导入日志失败");
        }
        return result;
    }
}
