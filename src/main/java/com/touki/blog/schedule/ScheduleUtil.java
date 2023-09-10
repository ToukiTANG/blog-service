package com.touki.blog.schedule;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

/**
 * @author Touki
 */
@Slf4j
public class ScheduleUtil {
    private final static String JOB_NAME = "TASK_";

    /**
     * 获取触发器key
     */
    public static TriggerKey getTriggerKey(Long jobId) {
        return TriggerKey.triggerKey(JOB_NAME + jobId);
    }

    /**
     * 获取jobKey
     */
    public static JobKey getJobKey(Long jobId) {
        return JobKey.jobKey(JOB_NAME + jobId);
    }

    /**
     * 获取表达式触发器
     */
    public static CronTrigger getCronTrigger(Scheduler scheduler, Long jobId) {
        try {
            return (CronTrigger) scheduler.getTrigger(getTriggerKey(jobId));
        } catch (SchedulerException e) {
            log.warn("获取定时任务CronTrigger出现异常");
            throw new RuntimeException(e);
        }
    }

    /**
     * 创建定时任务
     */
    public static void createScheduleJob(Scheduler scheduler, ScheduleJob scheduleJob) {
        try {
            // 构建job信息
            JobDetail jobDetail =
                    JobBuilder.newJob(ScheduleJobExecutor.class).withIdentity(getJobKey(scheduleJob.getJobId())).build();
            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder =
                    CronScheduleBuilder.cronSchedule(scheduleJob.getCron()).withMisfireHandlingInstructionDoNothing();
            // 按新的cronExpression表达式构建一个新的trigger
            CronTrigger trigger =
                    TriggerBuilder.newTrigger().withIdentity(getTriggerKey(scheduleJob.getJobId())).withSchedule(scheduleBuilder).build();
            // 放入参数，运行时的方法可以获取
            jobDetail.getJobDataMap().put(ScheduleJob.JOB_PARAM_KEY, scheduleJob);
            scheduler.scheduleJob(jobDetail, trigger);
            if (!scheduleJob.getStatus()) {
                pauseJob(scheduler, scheduleJob.getJobId());
            }
        } catch (SchedulerException e) {
            log.warn("创建定时任务失败");
            throw new RuntimeException(e);
        }
    }

    /**
     * 暂停任务
     */
    public static void pauseJob(Scheduler scheduler, Long jobId) {
        try {
            scheduler.pauseJob(getJobKey(jobId));
        } catch (SchedulerException e) {
            log.warn("暂停定时任务失败，任务id：{}", jobId);
            throw new RuntimeException(e);
        }
    }

    /**
     * 更新定时任务
     */
    public static void updateScheduleJob(Scheduler scheduler, ScheduleJob scheduleJob) {
        try {
            TriggerKey triggerKey = getTriggerKey(scheduleJob.getJobId());
            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder =
                    CronScheduleBuilder.cronSchedule(scheduleJob.getCron()).withMisfireHandlingInstructionDoNothing();
            CronTrigger trigger = getCronTrigger(scheduler, scheduleJob.getJobId());
            // 按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            // 参数
            trigger.getJobDataMap().put(ScheduleJob.JOB_PARAM_KEY, scheduleJob);
            scheduler.rescheduleJob(triggerKey, trigger);
            if (!scheduleJob.getStatus()) {
                pauseJob(scheduler, scheduleJob.getJobId());
            }
        } catch (SchedulerException e) {
            log.warn("更新定时任务失败，任务id：{}", scheduleJob.getJobId());
            throw new RuntimeException(e);
        }
    }
}
