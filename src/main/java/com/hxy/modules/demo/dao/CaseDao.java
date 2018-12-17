package com.hxy.modules.demo.dao;

import com.hxy.modules.common.dao.BaseDao;
import com.hxy.modules.demo.entity.CaseEntity;
import com.hxy.modules.demo.entity.LeaveEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 案件流程测试
 * 
 * @author hxy
 * @email huangxianyuan@gmail.com
 * @date 2017-07-31 13:33:23
 */
@Mapper
public interface CaseDao extends BaseDao<CaseEntity> {
	
}
