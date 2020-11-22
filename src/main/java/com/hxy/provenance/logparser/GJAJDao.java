package com.hxy.provenance.logparser;

import com.hxy.modules.common.dao.BaseDao;
import com.hxy.modules.demo.entity.CaseEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 案件流程测试
 *
 * @author hxy
 * @email huangxianyuan@gmail.com
 * @date 2017-07-31 13:33:23
 */
@Mapper
public interface GJAJDao extends BaseDao<GJAJEntity> {
    List<GJAJEntity> queryObject2(String ajlb);

    void updateComplete(int IS_COMPLETE,String BMSAH);
}
