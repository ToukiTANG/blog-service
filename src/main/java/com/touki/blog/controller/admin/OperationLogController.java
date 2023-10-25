package com.touki.blog.controller.admin;

import com.touki.blog.annotation.OperationLogger;
import com.touki.blog.model.entity.OperationLog;
import com.touki.blog.model.query.OperationLogQuery;
import com.touki.blog.model.vo.PageResult;
import com.touki.blog.model.vo.Result;
import com.touki.blog.service.OperationLogService;
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
public class OperationLogController {

    private final OperationLogService operationLogService;

    public OperationLogController(OperationLogService operationLogService) {
        this.operationLogService = operationLogService;
    }

    @GetMapping("/operationLog/list")
    public Result operationLogList(OperationLogQuery query) {
        PageResult<OperationLog> pageResult = operationLogService.getLogList(query);
        return Result.data(pageResult);
    }

    @PostMapping("/operationLog/delete")
    @OperationLogger("删除操作日志")
    @Transactional(rollbackFor = Exception.class)
    public Result deleteOperationLog(Long logId) {
        operationLogService.removeById(logId);
        return Result.success();
    }
}
