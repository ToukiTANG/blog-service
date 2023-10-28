package com.touki.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.touki.blog.model.entity.ExceptionLog;
import com.touki.blog.model.query.ExceptionLogQuery;
import com.touki.blog.model.vo.PageResult;
import org.springframework.scheduling.annotation.Async;

/**
 * @author Touki
 */
public interface ExceptionLogService extends IService<ExceptionLog> {
    /**
     * 保存controller接口异常信息
     *
     * @param log ExceptionLog
     */
    @Async
    void saveExceptionLog(ExceptionLog log);

    /**
     * 分页查询异常信息
     *
     * @param query ExceptionLogQuery
     * @return PageResult<ExceptionLog>
     */
    PageResult<ExceptionLog> exceptionLogList(ExceptionLogQuery query);
}
