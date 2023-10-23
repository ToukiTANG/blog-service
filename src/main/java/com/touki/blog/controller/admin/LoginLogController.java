package com.touki.blog.controller.admin;

import com.touki.blog.model.entity.LoginLog;
import com.touki.blog.model.query.LoginLogQuery;
import com.touki.blog.model.vo.PageResult;
import com.touki.blog.model.vo.Result;
import com.touki.blog.service.LoginLogService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Touki
 */
@RestController
@RequestMapping("/admin/logInLog")
public class LoginLogController {
    private final LoginLogService loginLogService;

    public LoginLogController(LoginLogService loginLogService) {
        this.loginLogService = loginLogService;
    }

    @GetMapping("/list")
    public Result loginLogs(LoginLogQuery query) {
        PageResult<LoginLog> pageResult = loginLogService.loginLogList(query);
        return Result.data(pageResult);
    }

    @PreAuthorize("hasAnyRole('admin')")
    @PostMapping("/delete")
    @Transactional(rollbackFor = Exception.class)
    public Result deleteLoginLog(Long logId) {
        loginLogService.removeById(logId);
        return Result.success();
    }
}
