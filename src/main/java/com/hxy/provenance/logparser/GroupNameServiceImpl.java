package com.hxy.provenance.logparser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("GroupNameService")
public class GroupNameServiceImpl implements GroupNameService{
    @Autowired
    private GroupNameDao groupNameDao;

    @Override
    public List<GroupNameEntity> queryObject() {
        return groupNameDao.queryObject();
    }
}
