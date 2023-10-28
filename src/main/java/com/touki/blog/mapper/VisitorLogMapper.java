package com.touki.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.touki.blog.model.dto.VisitLogStatistic;
import com.touki.blog.model.entity.VisitLog;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Touki
 */
@Repository
public interface VisitorLogMapper extends BaseMapper<VisitLog> {
    /**
     * 查询昨天的访问日志为统计DTO
     *
     * @return List<VisitLogStatistic>
     */
    List<VisitLogStatistic> visitLogStatisticYesterday();
}
