package com.hxy.provenance.logparser;

import com.hxy.modules.common.common.Constant;
import com.hxy.modules.common.exception.MyException;
import com.hxy.modules.common.page.Page;
import com.hxy.modules.common.page.PageHelper;
import com.hxy.modules.common.utils.CommUtils;
import com.hxy.modules.common.utils.Result;
import com.hxy.modules.common.utils.StringUtils;
import com.hxy.modules.common.utils.UserUtils;
import com.hxy.modules.demo.dao.CaseDao;
import com.hxy.modules.demo.entity.CaseEntity;
import com.hxy.modules.demo.service.CaseService;
import com.hxy.modules.sys.entity.UserEntity;
import com.hxy.provenance.neo4j.Neo4jFinalUtil;
import com.hxy.provenance.neo4j.NeoConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service("GJAJService")
public class GJAJServiceImpl implements GJAJService {
    @Autowired
    private GJAJDao caseDao;

    @Autowired
    private GJRZDao logDao;


    @Override
    public GJAJEntity queryObject(String id) {
        if (StringUtils.isEmpty(id)) {
            new MyException("id不为空!");
        }
        return caseDao.queryObject(id);
    }

    @Override
    public List<GJAJEntity> queryList(Map<String, Object> map) {
        return caseDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return caseDao.queryTotal(map);
    }

    @Override
    public void save(GJAJEntity leave) {
        caseDao.save(leave);
    }

    @Override
    public void update(GJAJEntity leave) {
        if (StringUtils.isEmpty(leave.getBMSAH())) {
            throw new MyException("案件环节id不能为空");
        }

        caseDao.update(leave);
    }

    @Override
    public int delete(String id) {
        if (StringUtils.isEmpty(id)) {
            throw new MyException("案件环节id不能为空");
        }
        return caseDao.delete(id);
    }

    @Override
    public void deleteBatch(String[] ids) {
        caseDao.deleteBatch(ids);
    }

    @Override
    public Page<GJAJEntity> findPage(GJAJEntity leaveEntity, int pageNum) {
        PageHelper.startPage(pageNum, Constant.pageSize);
        caseDao.queryList(leaveEntity);
        return PageHelper.endPage();
    }


    @Override
    public Result parseLog(String BMSAH) {
        //1. 创建一个案件头部节点
        GJAJEntity gjajEntity = caseDao.queryObject(BMSAH);
        String caseNodeId = GJNeo4jUtil.addCase(gjajEntity);

        //2. 从RZ_YX_RZ中查询该BMSAH所有的日志
        List<GJRZEntity> rzlist = logDao.queryList(BMSAH); //按时间升序排列
        try {
            for (GJRZEntity rz : rzlist) {
                String RZMS = rz.getRZMS();
                if (RZMS != null && RZMS.length() > 0) {
                    if (RZMS.startsWith("[") || RZMS.startsWith("{")) {

                        //解析json

                        //正则匹配键值对，字符型的值
                        Pattern r1 = Pattern.compile("\"(\\w+)\":\"{1}(.*?)\"{1}");
                        Matcher m1 = r1.matcher(RZMS);
                        while (m1.find()) {
                            String key = m1.group(1);
                            String value = m1.group(2);
                            System.out.println("xp>>>>>>>>>Found key: " + m1.group(1));
                            System.out.println("xp>>>>>>>>>Found value: " + m1.group(2));
                            Map<String, Object> par = new HashMap<>();
                            par.put(NeoConstants.KEY_LAST_NODE_ID, caseNodeId);
                            par.put(key, value);
                            par.put("name", key);
                            GJNeo4jUtil.addPropertyNode(BMSAH, key, "next", false, par);
                        }

                        //正则匹配键值对，整型的值
                        Pattern r2 = Pattern.compile("\"(\\w+)\":(\\w+)");
                        Matcher m2 = r2.matcher(RZMS);
                        while (m1.find()) {
                            String key = m2.group(1);
                            String value = m2.group(2);
                            System.out.println("xp>>>>>>>>>Found key: " + m2.group(1));
                            System.out.println("xp>>>>>>>>>Found value: " + m2.group(2));
                            Map<String, Object> par = new HashMap<>();
                            par.put(NeoConstants.KEY_LAST_NODE_ID, caseNodeId);
                            par.put(key, value);
                            par.put("name", key);
                            GJNeo4jUtil.addPropertyNode(BMSAH, key, "next", false, par);

                        }


                    } else {
                        //不是json，直接创建节点
                        Map<String, Object> map = new HashMap<>();
                        map.put(NeoConstants.KEY_LAST_NODE_ID, caseNodeId);
                        map.put("action", RZMS);
                        map.put("CZRM", rz.getCZRM());
                        GJNeo4jUtil.addActionNode(BMSAH, "ACTION", "next", false, map);
                    }
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.ok();
    }
}
