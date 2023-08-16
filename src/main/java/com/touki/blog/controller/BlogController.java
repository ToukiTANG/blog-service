package com.touki.blog.controller;

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
@RequestMapping("/blog")
public class BlogController {
    private final BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping("/list")
    public Result blogList(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                           @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        PageResult<BlogInfo> blogInfoPage = blogService.getBlogInfos(pageNum, pageSize);
        return Result.data(blogInfoPage);
    }
}
