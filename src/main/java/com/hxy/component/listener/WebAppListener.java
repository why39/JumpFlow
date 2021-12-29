package com.hxy.component.listener;

import com.hxy.modules.common.cache.CodeCache;
import com.hxy.modules.common.common.Constant;
import com.hxy.modules.common.utils.RedisUtil;
import com.hxy.modules.common.utils.StringUtils;
import com.hxy.modules.sys.dao.CodeDao;
import com.hxy.modules.sys.entity.CodeEntity;
import com.hxy.provenance.logparser.*;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class WebAppListener implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(WebAppListener.class);

    @Autowired
    ApplicationContext webApplicationContext;

    @Autowired
    private CodeDao codeDao;

    @Autowired
    private RedisUtil redisUtils;

    @Autowired
    private GJAJDao caseDao;

    /**
     * 实现EnvironmentAware接口，初始化系统数据。
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        codeCache();

        if (event.getApplicationContext().getParent() == null) {
            try {
                if (caseDao.openJob() == 1) {
                    //开关加到表tb_type_filed_count中，case_type=JobConifg,field=JobConifg,count_man=1
                    createQueryAJAndRZJob();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void createQueryAJAndRZJob() {
        //1创建job(任务)对象——做什么事
        JobDetail ajjob = JobBuilder.newJob(QueryAjJob.class).build();
        //2创建Trigger(触发器)对象——什么时间做  repeatSecondlyForever每秒执行一次—
        Trigger ajtrigger = TriggerBuilder.newTrigger().withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(30)).build();
        //3创建Scheduler(任务调度)对象
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.scheduleJob(ajjob, ajtrigger);
            scheduler.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        //1创建job(任务)对象——做什么事
        JobDetail rzjob = JobBuilder.newJob(QueryRZJob.class).build();
        //2创建Trigger(触发器)对象——什么时间做  repeatSecondlyForever每秒执行一次—
        Trigger rztrigger = TriggerBuilder.newTrigger().withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(30)).build();
        //3创建Scheduler(任务调度)对象
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.scheduleJob(rzjob, rztrigger);
            scheduler.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }


        //1创建job(任务)对象——做什么事
        JobDetail jdJobDetail = JobBuilder.newJob(QueryJDJob.class).build();
        //2创建Trigger(触发器)对象——什么时间做  repeatSecondlyForever每秒执行一次—
        Trigger jdtrigger = TriggerBuilder.newTrigger().withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(30)).build();
        //3创建Scheduler(任务调度)对象
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.scheduleJob(jdJobDetail, jdtrigger);
            scheduler.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 缓存全部数据字典
     */
    public void codeCache() {
        List<CodeEntity> allCode = codeDao.queryAllCode();
        Map<String, Map<String, Object>> allMap = new HashMap<>();
        if (allCode != null && allCode.size() > 0) {
            Map<String, Object> attrMap = null;
            for (CodeEntity code : allCode) {
                attrMap = new HashMap<>();
                attrMap.put("id", code.getId());
                attrMap.put("value", code.getValue());
                attrMap.put("childs", code.getChilds());
                attrMap.put("mark", code.getMark());
                attrMap.put("name", code.getName());
                allMap.put(code.getMark(), attrMap);
            }
        }
        for (String key : allMap.keySet()) {
            //父字典
            Map<String, Object> parentMap = allMap.get(key);
            String childStr = (String) parentMap.get("childs");
            if (StringUtils.isEmpty(childStr)) {
                continue;
            }
            String[] split = childStr.split(",");
            List<Map<String, Object>> childMaps = new ArrayList<>();

            for (String str : split) {
                //子字典
                Map<String, Object> childMap = allMap.get(str);
                childMaps.add(childMap);
            }
            //将所有子字典设置到父级字典
            parentMap.put("childList", childMaps);
        }
        CodeCache.put(Constant.CODE_CACHE, allMap);
        try {
            redisUtils.setObject(Constant.CODE_CACHE, allMap);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
