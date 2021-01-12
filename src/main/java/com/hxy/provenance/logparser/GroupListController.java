package com.hxy.provenance.logparser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequestMapping("demo/gl")
@Controller
public class GroupListController {
    @Autowired
    private GroupListService groupListService;

    @RequestMapping(value = "list")
    @ResponseBody
    public List<GroupListEntity> AllList(){
        List<GroupListEntity> grouplistEntity = groupListService.queryObject();
        return grouplistEntity;
    }
}
