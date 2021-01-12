package com.hxy.provenance.logparser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("GroupListService")
public class GroupListServiceImpl implements GroupListService{
    @Autowired
    private GroupListDao groupListDao;
    @Override
    public List<GroupListEntity> queryObject() {
        return groupListDao.queryObject();
    }
}
