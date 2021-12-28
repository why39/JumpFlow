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

public class QueryJDJob implements Job {

    Logger logger = LoggerFactory.getLogger(QueryJDJob.class);

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final String FORMAT = "%s %s";
    public static final String START_TIME = "00:00:01";
    public static final String END_TIME = "23:59:59";
    public static final String REDIS_KEY = "JD_LAST_QUERY_DATE";

    DataPlatformService dataPlatformService=  SpringContextUtils.getBean(DataPlatformService.class);

    private RedisUtil redisUtil = (RedisUtil) SpringContextUtils.getBean("redisUtil");

    GJAJDao caseDao = SpringContextUtils.getBean(GJAJDao.class);

    /**
     * 任务被触发时执行的方法
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        if (caseDao.openJob() == 0) {
            return;
        }
        logger.info("EXECUTE...QueryJDJob");
        LocalDate date = LocalDate.parse("2021-05-01");
        try {
            String dateStr = redisUtil.getString(REDIS_KEY);
            if (!StringUtils.isEmpty(dateStr)) {
                date = LocalDate.parse(dateStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        dataPlatformService.queryLCHJ(String.format(FORMAT, formatter.format(date), START_TIME), String.format(FORMAT, formatter.format(date), END_TIME));
        LocalDate newDate = date.plusDays(1l);
        try {
            redisUtil.setString(REDIS_KEY, formatter.format(newDate).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
