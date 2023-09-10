package com.touki.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.touki.blog.mapper.ScheduleJobLogMapper;
import com.touki.blog.model.entity.ScheduleJobLog;
import com.touki.blog.service.ScheduleJobLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Touki
 */
@Service
public class ScheduleJobLogServiceImpl extends ServiceImpl<ScheduleJobLogMapper, ScheduleJobLog> implements ScheduleJobLogService {
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveJobLog(ScheduleJobLog jobLog) {
        this.save(jobLog);
    }
}
