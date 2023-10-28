package com.touki.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.touki.blog.model.dto.VisitLogStatistic;
import com.touki.blog.model.entity.VisitLog;
import com.touki.blog.model.query.VisitLogQuery;
import com.touki.blog.model.vo.PageResult;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

/**
 * @author Touki
 */
public interface VisitorLogService extends IService<VisitLog> {
    /**
     * 保存访客日志
     *
     * @param visitLog VisitLog
     */
    @Async
    void saveLog(VisitLog visitLog);

    /**
     * 分页查询访问日志
     *
     * @param query VisitLogQuery
     * @return PageResult<VisitLog>
     */
    PageResult<VisitLog> visitLogList(VisitLogQuery query);

    /**
     * 查询昨天的访问记录并封装为统计DTO
     *
     * @return List<VisitLogStatistic>
     */
    List<VisitLogStatistic> visitLogStatisticYesterday();
}
