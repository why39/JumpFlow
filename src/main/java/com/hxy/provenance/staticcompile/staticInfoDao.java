package com.hxy.provenance.staticcompile;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface staticInfoDao {

    /**
     * 查询单个静态流程结构信息
     *
     * @param nid 节点编号
     * @return
     */

    staticInfoEntity querySingleInfo(String nid);

    /**
     * 查询某类业务的所有静态流程结构信息
     *
     * @param category 业务类别
     * @return
     */

    List<staticInfoEntity> queryCategoryInfo(String category);
}
