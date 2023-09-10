package com.touki.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.touki.blog.model.dto.NewScheduleJob;
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
}
