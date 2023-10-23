package com.touki.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.touki.blog.model.entity.LoginLog;
import com.touki.blog.model.query.LoginLogQuery;
import com.touki.blog.model.vo.PageResult;
import org.springframework.scheduling.annotation.Async;

/**
 * @author Touki
 */
public interface LoginLogService extends IService<LoginLog> {
    /**
     * 保存登录日志
     *
     * @param loginLog LoginLog
     */
    @Async
    void saveLog(LoginLog loginLog);

    /**
     * 分页查询登录日志
     *
     * @param query LoginLogQuery
     * @return PageResult<LoginLog>
     */
    PageResult<LoginLog> loginLogList(LoginLogQuery query);
}
