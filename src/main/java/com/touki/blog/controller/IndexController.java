package com.touki.blog.controller;

import com.touki.blog.constant.SiteDataConstant;
import com.touki.blog.entity.Category;
import com.touki.blog.entity.Tag;
import com.touki.blog.entity.vo.IndexInfo;
import com.touki.blog.entity.vo.NewBlog;
import com.touki.blog.entity.vo.RandomBlog;
import com.touki.blog.entity.vo.Result;
import com.touki.blog.service.BlogService;
import com.touki.blog.service.CategoryService;
import com.touki.blog.service.SiteSettingService;
import com.touki.blog.service.TagsService;
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
    private final CategoryService categoryService;
    private final TagsService tagsService;

    public IndexController(SiteSettingService siteSettingService, BlogService blogService, CategoryService categoryService, TagsService tagsService) {
        this.siteSettingService = siteSettingService;
        this.blogService = blogService;
        this.categoryService = categoryService;
        this.tagsService = tagsService;
    }

    @GetMapping("/site")
    public Result getSite() {
        HashMap<String, Object> resultMap = siteSettingService.getSitSettings();
        Object siteInfo = resultMap.get("siteInfo");
        Object introduction = resultMap.get("introduction");
        List<NewBlog> newBlogList = blogService.getNewBlogs(SiteDataConstant.NEW_BLOG_SIZE);
        List<RandomBlog> randomBlogList = blogService.getRandomBlogs(SiteDataConstant.RANDOM_BLOG_SIZE);
        List<Category> categoryList = categoryService.list();
        List<Tag> tagList = tagsService.list();
        IndexInfo indexInfo = new IndexInfo();
        indexInfo.setSiteInfo(siteInfo);
        indexInfo.setIntroduction(introduction);
        indexInfo.setCategoryList(categoryList);
        indexInfo.setNewBlogList(newBlogList);
        indexInfo.setTagList(tagList);
        indexInfo.setRandomBlogList(randomBlogList);
        return Result.data(indexInfo);
    }
}
