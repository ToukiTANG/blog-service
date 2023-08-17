package com.touki.blog.controller;

import com.touki.blog.entity.vo.NewBlog;
import com.touki.blog.entity.vo.Result;
import com.touki.blog.service.BlogService;
import com.touki.blog.service.SiteSettingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

/**
 * @author Touki
 */
@RestController
public class IndexController {
    private final SiteSettingService siteSettingService;
    private final BlogService blogService;

    public IndexController(SiteSettingService siteSettingService, BlogService blogService) {
        this.siteSettingService = siteSettingService;
        this.blogService = blogService;
    }

    @GetMapping("/site")
    public Result getSite() {
        HashMap<String, Object> resultMap = siteSettingService.getSitSettings();
        List<NewBlog> newBlogList = blogService.getNewBlogs();
        resultMap.put("newBlogList", newBlogList);

        return Result.data(resultMap);
    }
}
