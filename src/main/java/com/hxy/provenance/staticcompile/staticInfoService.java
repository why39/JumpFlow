package com.hxy.provenance.staticcompile;

import com.hxy.modules.common.utils.Result;

import java.util.List;

public interface staticInfoService {

    staticInfoEntity querySingleInfo(String nid);

    List<staticInfoEntity> queryCategoryInfo(String category);

    Result insertNode(String category, String caseId);
}
