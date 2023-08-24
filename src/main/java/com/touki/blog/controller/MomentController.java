package com.touki.blog.controller;

import com.touki.blog.constant.SiteDataConstant;
import com.touki.blog.model.entity.Moment;
import com.touki.blog.model.vo.PageResult;
import com.touki.blog.model.vo.Result;
import com.touki.blog.service.MomentService;
import org.springframework.web.bind.annotation.*;

/**
 * @author Touki
 */
@RestController
@RequestMapping("/moment")
public class MomentController {
    private final MomentService momentService;

    public MomentController(MomentService momentService) {
        this.momentService = momentService;
    }

    @GetMapping
    public Result momentList(@RequestParam(defaultValue = "1") Integer pageNum) {
        PageResult<Moment> momentPageResult = momentService.momentPage(pageNum, SiteDataConstant.MOMENT_SIZE);
        return Result.data(momentPageResult);
    }

    @PostMapping("/like")
    public Result likeMoment(Long momentId) {
        momentService.likeMoment(momentId);
        return Result.success();
    }
}
