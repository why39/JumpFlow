package com.hxy.provenance.neo4j;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface CaseDataMapper {
    int addCase(CaseDataBean dataBean);

    int updateCaseProvData(CaseProvDataBean provDataBean);

    String queryCaseProvData(String case_id);

    List<CaseDataBean> queryCaseList(CaseDataBean caseDataBean);
}
