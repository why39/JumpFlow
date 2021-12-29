package com.hxy.provenance.logparser;

import com.hxy.modules.common.dao.BaseDao;
import com.hxy.modules.demo.entity.CaseEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    List<GJAJEntity> queryCompleted(String ajlb);

    void updateComplete(int IS_COMPLETE, String BMSAH);

    List<Map<String, Object>> countAJLB();

    void insertCountFields(@Param("case_type") String case_type, @Param("field") String field, @Param("count_man") int count_man);

    void updateCountFields(@Param("case_type") String case_type, @Param("field") String field, @Param("count") int count_man);

    /**
     * 查询某个类别所有案卡项个数
     *
     * @param case_type
     * @param size
     * @return
     */
    List<Map<String, Object>> countFields(@Param("case_type") String case_type, @Param("size") int size);

    /**
     * 查询某类别中某个案卡项是否存在
     *
     * @param case_type
     * @param field
     * @return
     */
    int hasField(@Param("case_type") String case_type, @Param("field") String field);

    int countField(@Param("case_type") String case_type, @Param("field") String field);

    List<Map<String, Integer>> openJob();

    List<GJAJEntity> searchList(String content);

}
