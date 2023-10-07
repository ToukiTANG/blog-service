package com.touki.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.touki.blog.constant.DelimiterConstant;
import com.touki.blog.mapper.ScheduleJobLogMapper;
import com.touki.blog.mapper.ScheduleJobMapper;
import com.touki.blog.model.dto.NewScheduleJob;
import com.touki.blog.model.entity.ScheduleJobLog;
import com.touki.blog.model.query.BaseQuery;
import com.touki.blog.model.query.JobLogQuery;
import com.touki.blog.model.vo.PageResult;
import com.touki.blog.schedule.ScheduleJob;
import com.touki.blog.schedule.ScheduleUtil;
import com.touki.blog.service.ScheduleJobService;
import org.apache.commons.lang3.StringUtils;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author Touki
 */
@Service
public class ScheduleJobServiceImpl extends ServiceImpl<ScheduleJobMapper, ScheduleJob> implements ScheduleJobService {

    private final Scheduler scheduler;
    private final ScheduleJobMapper jobMapper;
    private final ScheduleJobLogMapper logMapper;

    public ScheduleJobServiceImpl(Scheduler scheduler, ScheduleJobMapper jobMapper, ScheduleJobLogMapper logMapper) {
        this.scheduler = scheduler;
        this.jobMapper = jobMapper;
        this.logMapper = logMapper;
    }

    @PostConstruct
    public void init() {
        List<ScheduleJob> list = this.list();
        for (ScheduleJob scheduleJob : list) {
            CronTrigger cronTrigger = ScheduleUtil.getCronTrigger(scheduler, scheduleJob.getJobId());
            if (cronTrigger == null) {
                ScheduleUtil.createScheduleJob(scheduler, scheduleJob);
            } else {
                ScheduleUtil.updateScheduleJob(scheduler, scheduleJob);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createJob(NewScheduleJob newScheduleJob) {
        ScheduleJob scheduleJob = new ScheduleJob();
        BeanUtils.copyProperties(newScheduleJob, scheduleJob);
        this.save(scheduleJob);
        ScheduleUtil.createScheduleJob(scheduler, scheduleJob);
    }

    @Override
    public void execute(Long jobId) {
        ScheduleJob scheduleJob = this.getById(jobId);
        ScheduleUtil.run(scheduler, scheduleJob);
    }

    @Override
    public PageResult<ScheduleJob> jobs(BaseQuery query) {
        Page<ScheduleJob> scheduleJobPage = new Page<>(query.getPageNum(), query.getPageSize());
        scheduleJobPage = jobMapper.selectPage(scheduleJobPage, null);

        PageResult<ScheduleJob> pageResult = new PageResult<>();
        pageResult.setPageSize(query.getPageSize());
        pageResult.setTotal((int) scheduleJobPage.getTotal());
        pageResult.setDataList(scheduleJobPage.getRecords());
        return pageResult;
    }

    @Override
    public PageResult<ScheduleJobLog> jobLogs(JobLogQuery query) {
        String[] dates = StringUtils.split(query.getDate(), DelimiterConstant.COMMA);
        String startTime = null;
        String endTime = null;
        if (dates != null && dates.length == 2) {
            startTime = dates[0];
            endTime = dates[1];
        }

        Page<ScheduleJobLog> logPage = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<ScheduleJobLog> queryWrapper = new LambdaQueryWrapper<>();
        if (startTime != null && endTime != null) {
            queryWrapper.between(ScheduleJobLog::getCreateTime, startTime, endTime);
        }
        queryWrapper.orderByDesc(ScheduleJobLog::getCreateTime);
        logPage = logMapper.selectPage(logPage, queryWrapper);

        PageResult<ScheduleJobLog> pageResult = new PageResult<>();
        pageResult.setPageSize(query.getPageSize());
        pageResult.setTotal((int) logPage.getTotal());
        pageResult.setDataList(logPage.getRecords());
        return pageResult;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteJobLog(Long logId) {
        logMapper.deleteById(logId);
    }
}
