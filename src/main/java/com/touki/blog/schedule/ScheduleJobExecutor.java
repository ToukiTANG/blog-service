package com.touki.blog.schedule;

import com.touki.blog.model.entity.ScheduleJobLog;
import com.touki.blog.service.ScheduleJobLogService;
import com.touki.blog.service.impl.ScheduleJobLogServiceImpl;
import com.touki.blog.util.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;
import java.util.concurrent.*;

/**
 * @author Touki
 */
@Slf4j
public class ScheduleJobExecutor extends QuartzJobBean {
    // 核心线程数
    private static final int CORE_POOL_SIZE = 1;
    // 最大线程数
    private static final int MAX_POOL_SIZE = 1;
    // 阻塞队列长度
    private static final int QUEUE_CAPACITY = 1;
    // 超过 corePoolSize 线程数量的线程最大空闲时间
    private static final Long KEEP_ALIVE_TIME = 0L;
    // 创建一个单线程池
    private final ExecutorService EXECUTORSERVICE = new ThreadPoolExecutor(
            CORE_POOL_SIZE,
            MAX_POOL_SIZE,
            KEEP_ALIVE_TIME,
            TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<>(QUEUE_CAPACITY), new ThreadPoolExecutor.AbortPolicy());

    @Override
    protected void executeInternal(JobExecutionContext context) {
        // 从任务上下文中拿到任务
        ScheduleJob scheduleJob = (ScheduleJob) context.getMergedJobDataMap().get(ScheduleJob.JOB_PARAM_KEY);
        ScheduleJobLogService logService = SpringContextUtil.getBean(ScheduleJobLogServiceImpl.class);
        ScheduleJobLog jobLog = new ScheduleJobLog();
        jobLog.setJobId(scheduleJob.getJobId());
        jobLog.setBeanName(scheduleJob.getBeanName());
        jobLog.setParams(scheduleJob.getParams());
        jobLog.setMethodName(scheduleJob.getMethodName());
        jobLog.setCreateTime(new Date());
        // 任务开始
        long startTime = System.currentTimeMillis();
        log.info("定时任务准备执行，任务id：{}", scheduleJob.getJobId());
        // 创建一个线程执行任务
        try {
            ScheduleJobRunnable taskRunnable = new ScheduleJobRunnable(scheduleJob.getBeanName(),
                    scheduleJob.getMethodName(), scheduleJob.getParams());
            // 执行线程
            Future<?> future = EXECUTORSERVICE.submit(taskRunnable);
            future.get();
            // 统计时间
            long executeTime = System.currentTimeMillis() - startTime;
            jobLog.setTimeConsumed((int) executeTime);
            jobLog.setStatus(true);
            log.info("任务执行成功，任务id：{}，共耗时：{}毫秒", scheduleJob.getJobId(), executeTime);
        } catch (Exception e) {
            long executeTime = System.currentTimeMillis() - startTime;
            jobLog.setTimeConsumed((int) executeTime);
            jobLog.setStatus(false);
            jobLog.setErrorMsg(StringUtils.substring(e.toString(), 0, 2000));
            log.info("任务执行失败，任务id：{}", scheduleJob.getJobId());
            throw new RuntimeException(e);
        } finally {
            logService.saveJobLog(jobLog);
            EXECUTORSERVICE.shutdown();
        }
    }
}
