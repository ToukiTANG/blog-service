package com.touki.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.touki.blog.model.entity.OperationLog;
import com.touki.blog.model.query.OperationLogQuery;
import com.touki.blog.model.vo.PageResult;
import org.springframework.scheduling.annotation.Async;

/**
 * @author Touki
 */
public interface OperationLogService extends IService<OperationLog> {
    /**
     * 保存操作日志
     *
     * @param operationLog OperationLog
     */
    @Async
    void saveLog(OperationLog operationLog);

    /**
     * 分页查询操作日志
     *
     * @param query OperationLogQuery
     * @return PageResult<OperationLog>
     */
    PageResult<OperationLog> getLogList(OperationLogQuery query);
}
