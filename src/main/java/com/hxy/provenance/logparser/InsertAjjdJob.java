package com.hxy.provenance.logparser;

import com.hxy.modules.common.utils.RedisUtil;
import com.hxy.modules.common.utils.SpringContextUtils;
import com.hxy.modules.common.utils.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class InsertAjjdJob implements Job {

    Logger logger = LoggerFactory.getLogger(InsertAjjdJob.class);

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final String FORMAT = "%s %s";
    public static final String START_TIME = "00:00:01";
    public static final String END_TIME = "23:59:59";
    public static final String REDIS_KEY = "AJ_LAST_QUERY_DATE";

    DataPlatformService dataPlatformService = SpringContextUtils.getBean(DataPlatformService.class);


    private RedisUtil redisUtil = (RedisUtil) SpringContextUtils.getBean("redisUtil");

    GJAJDao caseDao = SpringContextUtils.getBean(GJAJDao.class);
    GJRZDao rzDao = SpringContextUtils.getBean(GJRZDao.class);

    /**
     * 任务被触发时执行的方法
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Map<String, Object> params = new HashMap<>();

        String[] existed = new String[]{"七星检不收受[2021]522421000001号", "七星检刑捕受[2021]522421000001号", "七星检刑捕受[2021]522421000002号", "七星检未捕受[2021]522421000001号", "东检刑诉受[2019]770000101702号", "东检刑诉受[2019]770000101703号", "东检刑诉受[2019]770000101704号", "东检刑诉受[2019]770000101705号", "东检刑诉受[2019]770000101706号", "东检技协受[2019]770000100015号", "东检检任[2019]770000100073号", "东检检任[2019]770000100074号", "东检检任[2019]770000100075号", "东检检任[2019]770000100076号", "东检检任[2019]770000100077号", "丹检执违受[2021]522636000001号", "义检刑再审受[2021]522301000001号", "义检刑捕受[2020]522301000017号", "义检刑捕受[2020]522301000018号", "义检刑捕受[2021]522301000001号", "义检刑提抗受[2021]522301000001号", "义检刑诉受[2020]522301000044号", "义检刑诉受[2020]522301000045号", "义检技录受[2021]522301000001号", "乌检刑捕受[2021]520112000001号", "乌检刑捕受[2021]520112000002号", "乌检刑诉受[2021]520112000001号", "乌检刑诉受[2021]520112000002号", "乌检未捕受[2021]520112000001号", "习检事上受[2021]520330000001号", "黔西南检检建受[2021]522300000001号", "黔西南检检建受[2021]522300000002号", "黔西南检检建受[2021]522300000003号", "黔西南检民公[2021]522300000001号", "黔西南检自侦线受[2021]522300000001号", "黔西南检技录受[2021]522300000007号", "黔西南检技录受[2021]522300000008号", "黔西南检技录受[2021]522300000009号", "黔西南检技录受[2021]522300000010号", "黔西南检技录受[2021]522300000011号", "黔西南检案上受[2021]522300000001号"};
        Set<String> bmsahSet = new HashSet<String>();
        for (String bmsah : existed) {
            bmsahSet.add(bmsah);
        }
        List<GJAJEntity> list = caseDao.queryList(params);

        for (GJAJEntity aj : list) {
            if (!bmsahSet.contains(aj.getBMSAH())) {
                GJRZEntity gjrzEntity = new GJRZEntity();
                gjrzEntity.setBMSAH(aj.getBMSAH());
                gjrzEntity.setCZRM("金海燕");
                gjrzEntity.setEJFL("TYYW_LCBA_YW_BL_LC_JD_JOB");
                gjrzEntity.setID(UUID.randomUUID().toString());
                gjrzEntity.setRZMS("受理");
                gjrzEntity.setZHXGSJ("2019-11-09 10:29:51");
                rzDao.save(gjrzEntity);

                GJRZEntity gjrzEntity1 = new GJRZEntity();
                gjrzEntity1.setBMSAH(aj.getBMSAH());
                gjrzEntity1.setCZRM("金海燕");
                gjrzEntity1.setEJFL("TYYW_LCBA_YW_BL_LC_JD_JOB");
                gjrzEntity1.setID(UUID.randomUUID().toString());
                gjrzEntity1.setRZMS("审查");
                gjrzEntity1.setZHXGSJ("2021-11-20 10:29:51");
                rzDao.save(gjrzEntity1);

                GJRZEntity gjrzEntity2 = new GJRZEntity();
                gjrzEntity2.setBMSAH(aj.getBMSAH());
                gjrzEntity2.setCZRM("金海燕");
                gjrzEntity2.setEJFL("TYYW_LCBA_YW_BL_LC_JD_JOB");
                gjrzEntity2.setID(UUID.randomUUID().toString());
                gjrzEntity2.setRZMS("审查");
                gjrzEntity2.setZHXGSJ("2021-12-25 10:29:51");
                rzDao.save(gjrzEntity2);
            }
        }
    }
}
