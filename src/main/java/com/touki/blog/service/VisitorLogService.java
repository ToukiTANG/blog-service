package com.touki.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.touki.blog.model.entity.VisitLog;
import org.springframework.scheduling.annotation.Async;

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
}
