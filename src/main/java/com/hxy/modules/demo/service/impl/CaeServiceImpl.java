package com.hxy.modules.demo.service.impl;

import com.hxy.modules.common.common.Constant;
import com.hxy.modules.common.exception.MyException;
import com.hxy.modules.common.page.Page;
import com.hxy.modules.common.page.PageHelper;
import com.hxy.modules.common.utils.StringUtils;
import com.hxy.modules.common.utils.UserUtils;
import com.hxy.modules.common.utils.CommUtils;
import com.hxy.modules.demo.dao.CaseDao;
import com.hxy.modules.demo.entity.CaseEntity;
import com.hxy.modules.demo.service.CaseService;
import com.hxy.modules.sys.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;


@Service("caseService")
public class CaeServiceImpl implements CaseService {
    @Autowired
    private CaseDao caseDao;

    @Override
    public CaseEntity queryObject(String id) {
        if (StringUtils.isEmpty(id)) {
            new MyException("id不为空!");
        }
        return caseDao.queryObject(id);
    }

    @Override
    public List<CaseEntity> queryList(Map<String, Object> map) {
        return caseDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return caseDao.queryTotal(map);
    }

    @Override
    public void save(CaseEntity leave) {
        UserEntity currentUser = UserUtils.getCurrentUser();
        leave.setCode(CommUtils.getCode("D"));
        leave.setStatus(Constant.ActStauts.DRAFT.getValue());
        leave.setCreateId(currentUser.getId());
        leave.setCreateTime(new Date());
        leave.setId(CommUtils.uuid());
        leave.setStatus(Constant.ActStauts.DRAFT.getValue());
        leave.setUserId(UserUtils.getCurrentUserId());
        leave.setBapid(currentUser.getBapid());
        leave.setBaid(currentUser.getBaid());//未改

        caseDao.save(leave);
    }

    @Override
    public void update(CaseEntity leave) {
        if (StringUtils.isEmpty(leave.getId())) {
            throw new MyException("案件环节id不能为空");
        }
        leave.setUpdateId(UserUtils.getCurrentUserId());
        leave.setUpdateTime(new Date());
        caseDao.update(leave);
    }

    @Override
    public int delete(String id) {
        if (StringUtils.isEmpty(id)) {
            throw new MyException("案件环节id不能为空");
        }
        return caseDao.delete(id);
    }

    @Override
    public void deleteBatch(String[] ids) {
        caseDao.deleteBatch(ids);
    }

    @Override
    public Page<CaseEntity> findPage(CaseEntity leaveEntity, int pageNum) {
        PageHelper.startPage(pageNum, Constant.pageSize);
        caseDao.queryList(leaveEntity);
        return PageHelper.endPage();
    }

}
