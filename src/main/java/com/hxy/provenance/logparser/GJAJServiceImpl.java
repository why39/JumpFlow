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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service("GJAJService")
public class GJAJServiceImpl implements GJAJService {
    Logger logger = LoggerFactory.getLogger(GJAJServiceImpl.class);

    public static DateTimeFormatter zhxgsjFormatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static DateTimeFormatter zhxgsjFormatter2 = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
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
    public List<GJAJEntity> queryObject2(String ajlb) {
        return caseDao.queryObject2(ajlb);
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
    public void saveAJ(GJAJEntity leave) {
        caseDao.save(leave);
    }

    @Override
    public void saveRZ(GJRZEntity rz) {
        logDao.save(rz);
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

    public Result deleteLog(String BMSAH) {
        caseDao.updateComplete(0, BMSAH);
        GJNeo4jUtil.delete(BMSAH);
        return Result.ok();
    }

    public List<Map<String, Object>> countAJLB() {
        return caseDao.countAJLB();
    }

    @Override
    public Result deleteTest(int count) {
        long start = System.currentTimeMillis();
        PageHelper.startPage(1, count);
        Map<String, Object> params = new HashMap<>();
        caseDao.queryList(params);
        Page<GJAJEntity> list = PageHelper.endPage();

        for (GJAJEntity gjajEntity : list.getResult()) {
            deleteLog(gjajEntity.getBMSAH());
        }

        return Result.ok(String.valueOf(System.currentTimeMillis() - start));
    }

    @Override
    public List<Map<String, Object>> countFields(String caseType, int size) {
        return caseDao.countFields(caseType, size);
    }

    @Override
    public List<GJAJEntity> searchList(String content) {
        return caseDao.searchList(content);
    }

    @Override
    public Result parseLogForTest(int count) {
        long start = System.currentTimeMillis();
        PageHelper.startPage(1, count);
        Map<String, Object> params = new HashMap<>();
        caseDao.queryList(params);
        Page<GJAJEntity> list = PageHelper.endPage();

        ExecutorService executors = Executors.newFixedThreadPool(4);
        for (GJAJEntity gjajEntity : list.getResult()) {
            executors.submit(new Runnable() {
                @Override
                public void run() {
                    parseLog(gjajEntity.getBMSAH());
                    logger.info(count + " : " + String.valueOf(System.currentTimeMillis() - start));
                }
            });
        }
        executors.shutdown();
        return Result.ok();
    }

    private ConcurrentHashMap<String, Integer> fieldCount = new ConcurrentHashMap<String, Integer>();

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
        List<Map<String, Object>> taskList = new ArrayList<>();
        try {
            //2.1 先添加流程信息：
            for (GJRZEntity lcrz : rzlist) {
                String LCRZMS = lcrz.getRZMS();
                if (LCRZMS != null && LCRZMS.length() > 0) {
                    if (!lcrz.getEJFL().isEmpty() && lcrz.getEJFL().startsWith(GJRZEntity.LC_PREFIX)) {
                        //流程环节节点
                        Map<String, Object> map = new HashMap<>();
                        map.put(NeoConstants.KEY_LAST_NODE_ID, caseNodeId);
                        map.put("name", LCRZMS);
                        map.put("操作人", lcrz.getCZRM());
                        map.put("日志ID", lcrz.getID());
                        String zhxgsj = LocalDateTime.parse(lcrz.getZHXGSJ(), zhxgsjFormatter1).format(zhxgsjFormatter2);
                        map.put("最后修改时间", zhxgsj);
                        String taskNodeId = GJNeo4jUtil.addTaskNode(BMSAH, "Task", "下一步", false, map);
                        GJNeo4jUtil.addUserNode(lcrz.getCZRM(), taskNodeId, "修改");
                        map.put("id", taskNodeId);
                        taskList.add(map);
                    }
                }
            }

            //2.2再添加其他日志信息：
            for (GJRZEntity rz : rzlist) {
                String RZMS = rz.getRZMS();
                if (RZMS != null && RZMS.length() > 0) {
                    if (rz.getEJFL().isEmpty() || !rz.getEJFL().startsWith(GJRZEntity.LC_PREFIX)) {
                        if (RZMS.startsWith("[") || RZMS.startsWith("{")) {

                            //解析json

                            //正则匹配键值对，字符型的值
                            Pattern r1 = Pattern.compile("\"(\\w+)\":\"{1}(.*?)\"{1}");
                            Matcher m1 = r1.matcher(RZMS);
                            while (m1.find()) {
                                String key = m1.group(1);
                                String showKey = key;
                                String value = m1.group(2);
                                Map<String, Object> par = new HashMap<>();
                                if (KVCache.kv.containsKey(key.toUpperCase())) {
                                    showKey = KVCache.kv.get(key.toUpperCase()).cn;
                                    par.put("CN_KEY", showKey);
                                    par.put("案卡项类型", KVCache.kv.get(key.toUpperCase()).category);
                                }
                                par.put(key, value);
                                par.put("CaseNodeId", caseNodeId);
                                par.put("name", showKey);
                                par.put("最后修改时间", (String) rz.getZHXGSJ());
                                par.put("操作人", rz.getCZRM());
                                par.put("日志ID", rz.getID());

                                //根据修该时间判断流程节点
                                int relatedTaskId = taskList.size() - 1;
                                for (int i = 0; i < taskList.size(); i++) {
                                    Map<String, Object> task = taskList.get(i);
                                    String zhxgsj = (String) task.get("最后修改时间");
                                    String rzZhxgsj = LocalDateTime.parse(rz.getZHXGSJ(), zhxgsjFormatter1).format(zhxgsjFormatter2);
                                    if (Long.valueOf(rzZhxgsj) < Long.valueOf(zhxgsj)) {
                                        relatedTaskId = i;
                                        break;
                                    }
                                }

                                if (relatedTaskId >= 0) {
                                    Map<String, Object> lastTask = taskList.get(relatedTaskId);
                                    par.put("taskNodeId", lastTask.get("id"));
                                    par.put("taskNodeName", lastTask.get("name"));
                                }

                                String taskNodeId = GJNeo4jUtil.addPropertyNode(BMSAH, key, "变化", false, par, fieldCount);
                                GJNeo4jUtil.addUserNode(rz.getCZRM(), taskNodeId, "修改");
                            }

                            //正则匹配键值对，整型的值
                            Pattern r2 = Pattern.compile("\"(\\w+)\":(\\w+)");
                            Matcher m2 = r2.matcher(RZMS);
                            while (m2.find()) {
                                String key = m2.group(1);
                                String showKey = key;
                                String value = m2.group(2);
                                Map<String, Object> par = new HashMap<>();
                                if (KVCache.kv.containsKey(key.toUpperCase())) {
                                    showKey = KVCache.kv.get(key.toUpperCase()).cn;
                                    par.put("CN_KEY", showKey);
                                    par.put("案卡项类型", KVCache.kv.get(key.toUpperCase()).category);
                                }
                                par.put(NeoConstants.KEY_LAST_NODE_ID, caseNodeId);
                                par.put(key, value);
                                par.put("CaseNodeId", caseNodeId);
                                par.put("name", showKey);
                                par.put("最后修改时间", (String) rz.getZHXGSJ());
                                par.put("操作人", rz.getCZRM());
                                par.put("日志ID", rz.getID());

                                int relatedTaskId = taskList.size() - 1;
                                for (int i = 0; i < taskList.size(); i++) {
                                    Map<String, Object> task = taskList.get(i);
                                    String zhxgsj = (String) task.get("最后修改时间");
                                    String rzZhxgsj = LocalDateTime.parse(rz.getZHXGSJ(), zhxgsjFormatter1).format(zhxgsjFormatter2);
                                    if (Long.valueOf(rzZhxgsj) < Long.valueOf(zhxgsj)) {
                                        relatedTaskId = i;
                                        break;
                                    }
                                }

                                if (relatedTaskId >= 0) {
                                    Map<String, Object> lastTask = taskList.get(relatedTaskId);
                                    par.put("taskNodeId", lastTask.get("id"));
                                    par.put("taskNodeName", lastTask.get("name"));
                                }

                                String taskNodeId = GJNeo4jUtil.addPropertyNode(BMSAH, key, "变化", false, par, fieldCount);
                                GJNeo4jUtil.addUserNode(rz.getCZRM(), taskNodeId, "修改");
                            }


                        } else {
                            //不是json，直接修改节点
                            Map<String, Object> map = new HashMap<>();
                            map.put(NeoConstants.KEY_LAST_NODE_ID, caseNodeId);
                            map.put("RZMS", RZMS);
                            map.put("name", "操作");
                            map.put("操作人", rz.getCZRM());
                            map.put("最后修改时间", (String) rz.getZHXGSJ());
                            map.put("日志ID", rz.getID());
                            String taskNodeId = GJNeo4jUtil.addActionNode(BMSAH, "操作", "相关", false, map, fieldCount);
                            GJNeo4jUtil.addUserNode(rz.getCZRM(), taskNodeId, "修改");
                        }
                    }


                }
            }
            caseDao.updateComplete(1, BMSAH);

            for (Map.Entry<String, Integer> field : fieldCount.entrySet()) {

                if (caseDao.hasField(gjajEntity.getAJLB_MC(), field.getKey()) > 0) {
                    //如果表里面有这个案件类型对应的field，直接累加
                    caseDao.updateCountFields(gjajEntity.getAJLB_MC(), field.getKey(), field.getValue());
                } else {
                    //否则插入新数据
                    caseDao.insertCountFields(gjajEntity.getAJLB_MC(), field.getKey(), field.getValue());
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.ok();
    }
}
