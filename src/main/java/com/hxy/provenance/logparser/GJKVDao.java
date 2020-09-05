package com.hxy.provenance.logparser;

import com.hxy.modules.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * 中英文映射表
 * 
 * @author hxy
 * @email huangxianyuan@gmail.com
 * @date 2017-07-31 13:33:23
 */
@Mapper
public interface GJKVDao extends BaseDao<KVEntity> {
}
