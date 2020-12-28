package com.hxy.provenance.logparser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("AKLBService")
public class AKLBServiceImpl implements AKLBService{
    @Autowired
    private AKLBDao caseDao;

    @Override
    public List<AKLBEntity> queryObject() {
       return  caseDao.queryObject3();
    }
}
