package com.hxy.provenance.logparser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequestMapping("demo/gn")
@Controller
public class GroupNameController {
    @Autowired
    private GroupNameService groupnameService;

    @RequestMapping(value = "list")
    @ResponseBody
    public List<GroupNameEntity> AllList(){
        List<GroupNameEntity> groupnameEntity = groupnameService.queryObject();
        return groupnameEntity;
    }
}
