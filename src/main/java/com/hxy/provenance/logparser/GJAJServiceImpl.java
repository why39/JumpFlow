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

        //1. 修改一个案件头部节点
        GJAJEntity gjajEntity = caseDao.queryObject(BMSAH);

        if (!StringUtils.isEmpty(GJNeo4jUtil.queryCase(gjajEntity))) {
            return Result.ok();
        }

        String caseNodeId = GJNeo4jUtil.addCase(gjajEntity);

        //2. 从RZ_YX_RZ中查询该BMSAH所有的日志
        List<GJRZEntity> rzlist = logDao.queryList(BMSAH); //按时间升序排列
        try {
            //2.1 先添加流程信息：
            for (GJRZEntity lcrz : rzlist) {
                String LC日志内容 = lcrz.getRZMS();
                if (LC日志内容 != null && LC日志内容.length() > 0) {
                    if (!lcrz.getEJFL().isEmpty() && lcrz.getEJFL().startsWith(GJRZEntity.LC_PREFIX)) {
                        //流程环节节点
                        Map<String, Object> map = new HashMap<>();
                        map.put(NeoConstants.KEY_LAST_NODE_ID, caseNodeId);
                        map.put("name", LC日志内容);
                        map.put("操作人", lcrz.getCZRM());
                        map.put("日志ID", lcrz.getID());
                        String taskNodeId = GJNeo4jUtil.addTaskNode(BMSAH, "Task", "next", false, map);
                        GJNeo4jUtil.addUserNode(lcrz.getCZRM(), taskNodeId, "修改");
                    }
                }
            }

            //2.2再添加其他日志信息：
            for (GJRZEntity rz : rzlist) {
                String 日志内容 = rz.getRZMS();
                if (日志内容 != null && 日志内容.length() > 0) {
                    if (rz.getEJFL().isEmpty() || !rz.getEJFL().startsWith(GJRZEntity.LC_PREFIX)) {
                        if (日志内容.startsWith("[") || 日志内容.startsWith("{")) {

                            //解析json

                            //正则匹配键值对，字符型的值
                            Pattern r1 = Pattern.compile("\"(\\w+)\":\"{1}(.*?)\"{1}");
                            Matcher m1 = r1.matcher(日志内容);
                            while (m1.find()) {
                                String key = m1.group(1);
                                String showKey=key;
                                String value = m1.group(2);
                                System.out.println("xp>>>>>>>>>Found key: " + key);
                                System.out.println("xp>>>>>>>>>Found value: " + value);
                                Map<String, Object> par = new HashMap<>();
                                if(KVCache.kv.containsKey(key.toUpperCase())) {
                                    showKey=KVCache.kv.get(key.toUpperCase());
                                    par.put("CN_KEY", showKey);
                                }
                                par.put(key, value);
                                par.put("CaseNodeId", caseNodeId);
                                par.put("name", showKey);
                                par.put("操作人", rz.getCZRM());
                                par.put("日志ID", rz.getID());
                                String taskNodeId = GJNeo4jUtil.addPropertyNode(BMSAH, key, "next", false, par);
                                GJNeo4jUtil.addUserNode(rz.getCZRM(), taskNodeId, "修改");
                            }

                            //正则匹配键值对，整型的值
                            Pattern r2 = Pattern.compile("\"(\\w+)\":(\\w+)");
                            Matcher m2 = r2.matcher(日志内容);
                            while (m2.find()) {
                                String key = m2.group(1);
                                String showKey=key;
                                String value = m2.group(2);
                                System.out.println("xp>>>>>>>>>Found key: " + key);
                                System.out.println("xp>>>>>>>>>Found value: " + value);
                                Map<String, Object> par = new HashMap<>();
                                if(KVCache.kv.containsKey(key.toUpperCase())) {
                                    showKey=KVCache.kv.get(key.toUpperCase());
                                    par.put("CN_KEY", showKey);
                                }
                                par.put(NeoConstants.KEY_LAST_NODE_ID, caseNodeId);
                                par.put(key, value);
                                par.put("CaseNodeId", caseNodeId);
                                par.put("name", showKey);
                                par.put("操作人", rz.getCZRM());
                                par.put("日志ID", rz.getID());
                                String taskNodeId = GJNeo4jUtil.addPropertyNode(BMSAH, key, "next", false, par);
                                GJNeo4jUtil.addUserNode(rz.getCZRM(), taskNodeId, "修改");
                            }


                        } else {
                            //不是json，直接修改节点
                            Map<String, Object> map = new HashMap<>();
                            map.put(NeoConstants.KEY_LAST_NODE_ID, caseNodeId);
                            map.put("日志内容", 日志内容);
                            map.put("name", "ACTION");
                            map.put("操作人", rz.getCZRM());
                            map.put("日志ID", rz.getID());
                            String taskNodeId = GJNeo4jUtil.addActionNode(BMSAH, "ACTION", "next", false, map);
                            GJNeo4jUtil.addUserNode(rz.getCZRM(), taskNodeId, "修改");
                        }
                    }


                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.ok();
    }
}
