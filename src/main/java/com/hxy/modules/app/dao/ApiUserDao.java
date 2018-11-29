package com.hxy.modules.app.dao;

import com.hxy.modules.app.entity.ApiUserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 系统用户表
 * 
 * @author chenshun
 * @email huangxianyuan@gmail.com
 * @date 2017-05-03 09:41:38
 */
@Mapper
public interface ApiUserDao{
    /**
     * 根据id获取用户相关信息
     * @param id
     * @return
     */
    ApiUserEntity userInfo(String id);
}
