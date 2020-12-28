package com.hxy.provenance.logparser;

import com.hxy.modules.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 案件类别
 *
 * @author zyq
 * @date 2020-12-27 13:33:23
 */
@Mapper
public interface AKLBDao extends BaseDao<AKLBEntity> {
    List<AKLBEntity> queryObject3();
}
