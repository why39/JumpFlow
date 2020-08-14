package com.hxy.provenance.logparser;

import com.hxy.modules.common.common.Constant;
import com.hxy.modules.common.exception.MyException;
import com.hxy.modules.common.page.Page;
import com.hxy.modules.common.page.PageHelper;
import com.hxy.modules.common.utils.CommUtils;
import com.hxy.modules.common.utils.StringUtils;
import com.hxy.modules.common.utils.UserUtils;
import com.hxy.modules.demo.dao.CaseDao;
import com.hxy.modules.demo.entity.CaseEntity;
import com.hxy.modules.demo.service.CaseService;
import com.hxy.modules.sys.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;


@Service("GJAJService")
public class GJAJServiceImpl implements GJAJService {
    @Autowired
    private GJAJDao caseDao;

    @Autowired
    private GJRZDao logDao;


    @Override
    public GJAJEntity queryObject(String id) {
        if (StringUtils.isEmpty(id)) {
            new MyException("id不为空!");
        }
        return caseDao.queryObject(id);
    }

    @Override
    public List<GJAJEntity> queryList(Map<String, Object> map) {
        return caseDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return caseDao.queryTotal(map);
    }

    @Override
    public void save(GJAJEntity leave) {
        caseDao.save(leave);
    }

    @Override
    public void update(GJAJEntity leave) {
        if (StringUtils.isEmpty(leave.getBMSAH())) {
            throw new MyException("案件环节id不能为空");
        }

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
    public Page<GJAJEntity> findPage(GJAJEntity leaveEntity, int pageNum) {
        PageHelper.startPage(pageNum, Constant.pageSize);
        caseDao.queryList(leaveEntity);
        return PageHelper.endPage();
    }


    @Override
    public List<GJRZEntity> parseLog(String BMSAH) {
        //1. 从RZ_YX_RZ中查询该BMSAH所有的日志
        List<GJRZEntity> rzlist = logDao.queryList(BMSAH);
        for (GJRZEntity rz : rzlist) {
            String RZMS = rz.getRZMS();


        }
        return rzlist;
    }
}
