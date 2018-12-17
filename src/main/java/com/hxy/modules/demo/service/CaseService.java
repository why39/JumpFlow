package com.hxy.modules.demo.service;


import com.hxy.modules.common.page.Page;
import com.hxy.modules.demo.entity.CaseEntity;

import java.util.List;
import java.util.Map;

/**
 * 案件环节流程测试
 *
 * @author hxy
 * @email huangxianyuan@gmail.com
 * @date 2017-07-31 13:33:23
 */
public interface CaseService {

    CaseEntity queryObject(String id);

    List<CaseEntity> queryList(Map<String, Object> map);

    int queryTotal(Map<String, Object> map);

    void save(CaseEntity leave);

    void update(CaseEntity leave);

    int delete(String id);

    void deleteBatch(String[] ids);

    Page<CaseEntity> findPage(CaseEntity leaveEntity, int pageNum);
}
