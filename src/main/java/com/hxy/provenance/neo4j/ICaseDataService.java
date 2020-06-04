package com.hxy.provenance.neo4j;

import com.hxy.provenance.neo4j.json.JSONObject;

import java.util.List;

public interface ICaseDataService {
    int addCase(CaseDataBean dataBean);

    void updateCaseProvData(String case_id, String case_prov_data);

    void updateCaseProvData(String case_id, JSONObject case_prov_data);

    JSONObject queryCaseProvData(String case_id);

    List<CaseDataBean> queryCaseList(CaseDataBean caseDataBean);
}
