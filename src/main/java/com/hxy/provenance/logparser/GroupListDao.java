package com.hxy.provenance.logparser;

import com.hxy.modules.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface GroupListDao extends BaseDao<GroupListEntity> {
    List<GroupListEntity> queryObject();
}
