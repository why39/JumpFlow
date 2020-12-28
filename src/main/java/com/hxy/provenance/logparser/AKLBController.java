package com.hxy.provenance.logparser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequestMapping("demo/ak")
@Controller
public class AKLBController {
    @Autowired
    private AKLBService aklbService;

    @RequestMapping(value = "aklb")
    @ResponseBody
    public List<AKLBEntity> Aklb(){
        List<AKLBEntity> aklbEntity = aklbService.queryObject();
        return aklbEntity;
    }
}
