package com.touki.blog.controller.admin;

import com.touki.blog.model.entity.Category;
import com.touki.blog.model.entity.Tag;
import com.touki.blog.model.query.AdminBlogQuery;
import com.touki.blog.model.vo.BlogInfo;
import com.touki.blog.model.vo.PageResult;
import com.touki.blog.model.vo.Result;
import com.touki.blog.service.BlogService;
import com.touki.blog.service.CategoryService;
import com.touki.blog.service.TagsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

/**
 * @author Touki
 */
@RequestMapping("/admin")
@RestController
public class AdminBlogController {
    private final BlogService blogService;
    private final CategoryService categoryService;
    private final TagsService tagsService;

    public AdminBlogController(BlogService blogService, CategoryService categoryService, TagsService tagsService) {
        this.blogService = blogService;
        this.categoryService = categoryService;
        this.tagsService = tagsService;
    }

    @GetMapping("/blogs")
    public Result blogs(AdminBlogQuery query) {
        PageResult<BlogInfo> blogPageResult = blogService.adminBlogs(query);
        List<Category> categories = categoryService.list();
        HashMap<String, Object> map = new HashMap<>(4);
        map.put("blogs", blogPageResult);
        map.put("categories", categories);
        return Result.data(map);
    }

    @GetMapping("/categoryAndTag")
    public Result categoryAndTag() {
        List<Category> categories = categoryService.list();
        List<Tag> tags = tagsService.list();
        HashMap<String, Object> map = new HashMap<>(4);
        map.put("categories", categories);
        map.put("tags", tags);
        return Result.data(map);
    }
}
