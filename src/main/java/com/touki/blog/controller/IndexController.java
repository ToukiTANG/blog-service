package com.touki.blog.controller;

import com.touki.blog.constant.RedisKeyConstant;
import com.touki.blog.constant.SiteDataConstant;
import com.touki.blog.model.entity.Category;
import com.touki.blog.model.entity.Tag;
import com.touki.blog.model.vo.IndexInfo;
import com.touki.blog.model.vo.NewBlog;
import com.touki.blog.model.vo.RandomBlog;
import com.touki.blog.model.vo.Result;
import com.touki.blog.service.*;
import com.touki.blog.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
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
    private final RedisService redisService;

    public IndexController(SiteSettingService siteSettingService, BlogService blogService,
                           CategoryService categoryService, TagsService tagsService, RedisService redisService) {
        this.siteSettingService = siteSettingService;
        this.blogService = blogService;
        this.categoryService = categoryService;
        this.tagsService = tagsService;
        this.redisService = redisService;
    }

    @GetMapping("/site")
    public Result getSite() {
        String jsonString = redisService.getValue(RedisKeyConstant.SITE_SETTING_INFO);
        if (!StringUtils.isBlank(jsonString)) {
            IndexInfo indexInfo = JsonUtil.readValue(jsonString, IndexInfo.class);
            return Result.data(indexInfo);
        }
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
        redisService.setValue(RedisKeyConstant.SITE_SETTING_INFO, JsonUtil.writeValueAsString(indexInfo));
        return Result.data(indexInfo);
    }
}
