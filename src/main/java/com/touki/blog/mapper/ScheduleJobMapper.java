package com.touki.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.touki.blog.schedule.ScheduleJob;
import org.springframework.stereotype.Repository;

/**
 * @author Touki
 */
@Repository
public interface ScheduleJobMapper extends BaseMapper<ScheduleJob> {
}
