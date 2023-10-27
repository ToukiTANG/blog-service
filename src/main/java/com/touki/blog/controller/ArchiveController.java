package com.touki.blog.controller;

import com.touki.blog.annotation.VisitLogger;
import com.touki.blog.constant.SiteDataConstant;
import com.touki.blog.enums.VisitBehaviorEnum;
import com.touki.blog.model.vo.ArchiveResult;
import com.touki.blog.model.vo.BlogInfo;
import com.touki.blog.model.vo.PageResult;
import com.touki.blog.model.vo.Result;
import com.touki.blog.service.BlogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Touki
 */
@RestController
@RequestMapping("/archive")
public class ArchiveController {
    public final BlogService blogService;

    public ArchiveController(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping
    public Result archive() {
        ArchiveResult archiveResult = blogService.getArchive();
        return Result.data(archiveResult);
    }

    @GetMapping("/yearMonth")
    @VisitLogger(VisitBehaviorEnum.ARCHIVE)
    public Result yearMonth(@RequestParam(defaultValue = "1") Long pageNum,
                            @RequestParam String yearMonth) {
        PageResult<BlogInfo> blogInfoPageResult = blogService.getBlogInfosByYearMonth(pageNum,
                SiteDataConstant.ARCHIVE_YEARMONTH_SIZE, yearMonth);
        return Result.data(blogInfoPageResult);
    }
}
