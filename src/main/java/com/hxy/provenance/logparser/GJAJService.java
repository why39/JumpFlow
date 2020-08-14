package com.hxy.provenance.logparser;


import com.hxy.modules.common.page.Page;
import com.hxy.modules.demo.entity.CaseEntity;

import java.util.List;
import java.util.Map;

public interface GJAJService {

    GJAJEntity queryObject(String id);

    List<GJAJEntity> queryList(Map<String, Object> map);

    int queryTotal(Map<String, Object> map);

    void save(GJAJEntity leave);

    void update(GJAJEntity leave);

    int delete(String id);

    void deleteBatch(String[] ids);

    Page<GJAJEntity> findPage(GJAJEntity leaveEntity, int pageNum);

    List<GJRZEntity> parseLog(String BMSAH);
}
