package com.touki.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.touki.blog.model.entity.ScheduleJobLog;

/**
 * @author Touki
 */
public interface ScheduleJobLogService extends IService<ScheduleJobLog> {
    /**
     * 保存日志
     *
     * @param jobLog 日志
     */
    void saveJobLog(ScheduleJobLog jobLog);
}
