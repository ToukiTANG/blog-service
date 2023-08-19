package com.touki.blog.controller;

import com.touki.blog.constant.SiteDataConstant;
import com.touki.blog.entity.vo.ArchiveResult;
import com.touki.blog.entity.vo.BlogInfo;
import com.touki.blog.entity.vo.PageResult;
import com.touki.blog.entity.vo.Result;
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
    public Result yearMonth(@RequestParam(defaultValue = "1") Long pageNum,
                            @RequestParam String yearMonth) {
        PageResult<BlogInfo> blogInfoPageResult = blogService.getBlogInfosByYearMonth(pageNum,
                SiteDataConstant.ARCHIVE_YEARMONTH_SIZE, yearMonth);
        return Result.data(blogInfoPageResult);
    }
}
