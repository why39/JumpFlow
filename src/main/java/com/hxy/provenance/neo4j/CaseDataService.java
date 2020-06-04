package com.hxy.provenance.neo4j;

import com.hxy.provenance.neo4j.json.JSON;
import com.hxy.provenance.neo4j.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hxy.provenance.neo4j.NeoConstants.*;


@Service
public class CaseDataService implements ICaseDataService {
    @Autowired
    private CaseDataMapper caseDataMapper;

    @Override
    public int addCase(CaseDataBean dataBean) {
        //同时添加一个neo4j的虚拟头节点，用于存储整个案件的信息，并且所有属性世系数据都从这个案件开始
        String curTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String caseId = curTime;
        Map<String, Object> params = new HashMap<>();
        if (dataBean.getCase_id() != null && !"".equals(dataBean.getCase_id())) {
            caseId = dataBean.getCase_id();
        }

        String createUserName = dataBean.getUser_name();
        String createUserId = dataBean.getUser_id();

        params.put("name", dataBean.getCase_name());
        params.put("case_name", dataBean.getCase_name());
        params.put("case_category", dataBean.getCase_category());
        params.put("create_user_name", createUserName);
        params.put("user_id", createUserId);

        String headId = Neo4jFinalUtil.createKeyValues(caseId, NeoConstants.TYPE_CASE_HEAD, "", "开始", false, params);


        String userNodeId = Neo4jFinalUtil.createUser(createUserName, createUserId, caseId, headId);

        CaseProvDataBean provDataBean = new CaseProvDataBean();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(KEY_CASE_HEAD_ID, headId);
        jsonObject.put(KEY_CASE_NEW_NODE_ID, headId);
        jsonObject.put(KEY_CASE_USER_ID, userNodeId);
        provDataBean.setCase_id(caseId);
        provDataBean.setCase_prov_data(jsonObject.toString());
        caseDataMapper.updateCaseProvData(provDataBean);
        return caseDataMapper.addCase(dataBean);
    }

    @Override
    public void updateCaseProvData(String case_id, String case_prov_data) {
        caseDataMapper.updateCaseProvData(new CaseProvDataBean(case_id, case_prov_data));
    }

    @Override
    public void updateCaseProvData(String case_id, JSONObject data) {
        this.updateCaseProvData(case_id, data.toString());
    }

    @Override
    public JSONObject queryCaseProvData(String case_id) {
        String data = caseDataMapper.queryCaseProvData(case_id);
        if (data != null && !"".equals(data)) {
            JSONObject jsonObject = null;
            try {
                jsonObject = JSON.unmarshal(data, JSONObject.class);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return jsonObject;
        }
        return null;
    }

    @Override
    public List<CaseDataBean> queryCaseList(CaseDataBean caseDataBean) {
        return caseDataMapper.queryCaseList(caseDataBean);
    }

}
