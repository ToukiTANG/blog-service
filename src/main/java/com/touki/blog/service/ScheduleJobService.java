package com.touki.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.touki.blog.model.dto.NewScheduleJob;
import com.touki.blog.model.query.BaseQuery;
import com.touki.blog.model.vo.PageResult;
import com.touki.blog.schedule.ScheduleJob;

/**
 * @author Touki
 */
public interface ScheduleJobService extends IService<ScheduleJob> {
    /**
     * 新建任务
     *
     * @param newScheduleJob NewScheduleJob
     */
    void createJob(NewScheduleJob newScheduleJob);

    /**
     * 立即执行一次定时任务
     *
     * @param jobId 任务id
     */
    void execute(Long jobId);

    /**
     * 分页查询定时任务列表
     *
     * @param query BaseQuery
     * @return PageResult<ScheduleJob>
     */
    PageResult<ScheduleJob> jobs(BaseQuery query);
}
