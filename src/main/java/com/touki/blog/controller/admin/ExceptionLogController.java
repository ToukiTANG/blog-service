package com.touki.blog.controller.admin;

import com.touki.blog.model.entity.ExceptionLog;
import com.touki.blog.model.query.ExceptionLogQuery;
import com.touki.blog.model.vo.PageResult;
import com.touki.blog.model.vo.Result;
import com.touki.blog.service.ExceptionLogService;
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
@RequestMapping("/admin")
public class ExceptionLogController {

    private final ExceptionLogService exceptionLogService;

    public ExceptionLogController(ExceptionLogService exceptionLogService) {
        this.exceptionLogService = exceptionLogService;
    }

    @GetMapping("/exceptionLog/list")
    public Result exceptionLogList(ExceptionLogQuery query) {
        PageResult<ExceptionLog> pageResult = exceptionLogService.exceptionLogList(query);
        return Result.data(pageResult);
    }

    @PostMapping("/exceptionLog/delete")
    @PreAuthorize("hasAnyRole('admin')")
    @Transactional(rollbackFor = Exception.class)
    public Result deleteExceptionLog(Long logId) {
        exceptionLogService.removeById(logId);
        return Result.success();
    }
}
