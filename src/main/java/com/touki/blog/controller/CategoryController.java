package com.touki.blog.controller;

import com.touki.blog.constant.SiteDataConstant;
import com.touki.blog.model.entity.Category;
import com.touki.blog.model.vo.BlogInfo;
import com.touki.blog.model.vo.PageResult;
import com.touki.blog.model.vo.Result;
import com.touki.blog.service.BlogService;
import com.touki.blog.service.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @author Touki
 */
@RestController
@RequestMapping("/category")
public class CategoryController {
    private final BlogService blogService;
    private final CategoryService categoryService;

    public CategoryController(BlogService blogService, CategoryService categoryService) {
        this.blogService = blogService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public Result blogInfosByCategoryId(@RequestParam Long categoryId,
                                        @RequestParam(defaultValue = "1") Integer pageNum) {
        PageResult<BlogInfo> blogInfoPage = blogService.getBlogInfosByCategoryId(categoryId, pageNum,
                SiteDataConstant.BLOG_CATEGORY_SIZE);
        Category category = categoryService.getById(categoryId);
        HashMap<String, Object> map = new HashMap<>(2);
        map.put("blogInfoPage", blogInfoPage);
        map.put("category", category);
        return Result.data(map);
    }
}
