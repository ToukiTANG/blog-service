package com.touki.blog.controller;

import com.touki.blog.annotation.VisitLogger;
import com.touki.blog.constant.SiteDataConstant;
import com.touki.blog.enums.VisitBehaviorEnum;
import com.touki.blog.model.entity.Tag;
import com.touki.blog.model.vo.BlogInfo;
import com.touki.blog.model.vo.PageResult;
import com.touki.blog.model.vo.Result;
import com.touki.blog.service.BlogService;
import com.touki.blog.service.TagsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @author Touki
 */
@RestController
@RequestMapping("/tag")
public class TagController {
    private final BlogService blogService;
    private final TagsService tagsService;

    public TagController(BlogService blogService, TagsService tagsService) {
        this.blogService = blogService;
        this.tagsService = tagsService;
    }

    @GetMapping
    @VisitLogger(VisitBehaviorEnum.TAG)
    public Result blogInfosByTagId(@RequestParam Long tagId,
                                   @RequestParam(defaultValue = "1") Integer pageNum) {
        PageResult<BlogInfo> blogInfoPage = blogService.getBlogInfosByTagId(tagId, pageNum,
                SiteDataConstant.BLOG_TAG_SIZE);
        Tag tag = tagsService.getById(tagId);
        HashMap<String, Object> map = new HashMap<>(2);
        map.put("blogInfoPage", blogInfoPage);
        map.put("tag", tag);
        return Result.data(map);
    }
}
