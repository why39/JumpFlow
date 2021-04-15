package com.hxy.provenance.logparser;


import com.hxy.modules.common.page.Page;
import com.hxy.modules.common.utils.Result;
import com.hxy.modules.demo.entity.CaseEntity;

import java.util.List;
import java.util.Map;

public interface GJAJService {

    GJAJEntity queryObject(String id);

    List<GJAJEntity> queryObject2(String ajlb);

    List<GJAJEntity> queryList(Map<String, Object> map);

    int queryTotal(Map<String, Object> map);

    void saveAJ(GJAJEntity leave);

    void saveRZ(GJRZEntity rz);

    void update(GJAJEntity leave);

    int delete(String id);

    void deleteBatch(String[] ids);

    Page<GJAJEntity> findPage(GJAJEntity leaveEntity, int pageNum);

    Result parseLog(String BMSAH);

    Result deleteLog(String bmsah);

    List<Map<String, Object>> countAJLB();

    Result parseLogForTest(int count);

    Result deleteTest(int count);

    List<Map<String,Object>> countFields(String caseType,int size);
}
