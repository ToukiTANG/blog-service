package com.touki.blog.controller.admin;

import com.touki.blog.annotation.OperationLogger;
import com.touki.blog.model.entity.Visitor;
import com.touki.blog.model.query.VisitorQuery;
import com.touki.blog.model.vo.PageResult;
import com.touki.blog.model.vo.Result;
import com.touki.blog.service.VisitorService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author Touki
 */
@RestController
@RequestMapping("/admin")
public class VisitorController {
    private final VisitorService visitorService;

    public VisitorController(VisitorService visitorService) {
        this.visitorService = visitorService;
    }

    @GetMapping("visitor/list")
    public Result visitorList(VisitorQuery query) {
        PageResult<Visitor> pageResult = visitorService.visitorList(query);
        return Result.data(pageResult);
    }

    @PostMapping("/visitor/delete")
    @PreAuthorize("hasAnyRole('admin')")
    @OperationLogger("删除访客")
    public Result deleteVisitor(@RequestParam Long visitorId, @RequestParam String uuid) {
        visitorService.deleteVisitor(visitorId, uuid);
        return Result.success();
    }
}
