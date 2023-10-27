package com.touki.blog.controller.admin;

import com.touki.blog.annotation.OperationLogger;
import com.touki.blog.model.entity.VisitLog;
import com.touki.blog.model.query.VisitLogQuery;
import com.touki.blog.model.vo.PageResult;
import com.touki.blog.model.vo.Result;
import com.touki.blog.service.VisitorLogService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Touki
 */
@RestController
@RequestMapping("/admin")
public class VisitLogController {
    private final VisitorLogService visitorLogService;

    public VisitLogController(VisitorLogService visitorLogService) {
        this.visitorLogService = visitorLogService;
    }

    @GetMapping("/visitLog/list")
    public Result visitLogList(VisitLogQuery query) {
        PageResult<VisitLog> pageResult = visitorLogService.visitLogList(query);
        return Result.data(pageResult);
    }

    @PostMapping("/visitLog/delete")
    @OperationLogger("删除访问日志")
    @PreAuthorize("hasAnyRole('admin')")
    public Result deleteVisitLog(Long logId) {
        visitorLogService.removeById(logId);
        return Result.success();
    }
}
