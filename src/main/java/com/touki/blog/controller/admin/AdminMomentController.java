package com.touki.blog.controller.admin;

import com.touki.blog.annotation.OperationLogger;
import com.touki.blog.model.dto.MomentUpdate;
import com.touki.blog.model.dto.NewMoment;
import com.touki.blog.model.entity.Moment;
import com.touki.blog.model.query.SimpleQuery;
import com.touki.blog.model.vo.PageResult;
import com.touki.blog.model.vo.Result;
import com.touki.blog.service.MomentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * @author Touki
 */
@RestController
@RequestMapping("/admin/moment")
public class AdminMomentController {
    private final MomentService momentService;

    public AdminMomentController(MomentService momentService) {
        this.momentService = momentService;
    }

    @PreAuthorize("hasAnyRole('admin')")
    @PostMapping("/save")
    @OperationLogger("新建动态")
    public Result saveMoment(@RequestBody NewMoment newMoment) {
        momentService.saveMoment(newMoment);
        return Result.success();
    }

    @GetMapping("/list")
    public Result momentList(SimpleQuery query) {
        PageResult<Moment> momentPageResult = momentService.adminMomentPage(query.getPageNum(), query.getPageSize());
        return Result.data(momentPageResult);
    }

    @PreAuthorize("hasAnyRole('admin')")
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/delete")
    @OperationLogger("删除动态")
    public Result deleteMoment(Long id) {
        momentService.removeById(id);
        return Result.success();
    }

    @GetMapping
    public Result getMoment(Long id) {
        return Result.data(momentService.getById(id));
    }

    @PreAuthorize("hasAnyRole('admin')")
    @PostMapping("/update")
    @OperationLogger("更新动态")
    public Result updateMoment(@RequestBody MomentUpdate momentUpdate) {
        momentService.updateMoment(momentUpdate);
        return Result.success();
    }

    @PreAuthorize("hasAnyRole('admin')")
    @PostMapping("/published")
    @OperationLogger("更新动态公开状态")
    public Result publishOrNot(Long momentId, Boolean published) {
        momentService.publishOrNot(momentId, published);
        return Result.success();
    }
}
