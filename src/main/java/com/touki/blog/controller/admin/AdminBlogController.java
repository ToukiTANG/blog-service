package com.touki.blog.controller.admin;

import com.touki.blog.annotation.RemoveRedisCache;
import com.touki.blog.exception.MyException;
import com.touki.blog.model.dto.BlogDTO;
import com.touki.blog.model.entity.Category;
import com.touki.blog.model.entity.Tag;
import com.touki.blog.model.query.AdminBlogQuery;
import com.touki.blog.model.vo.BlogDetail;
import com.touki.blog.model.vo.BlogInfo;
import com.touki.blog.model.vo.PageResult;
import com.touki.blog.model.vo.Result;
import com.touki.blog.service.BlogService;
import com.touki.blog.service.CategoryService;
import com.touki.blog.service.TagsService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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

    @PreAuthorize("hasAnyRole('admin')")
    @PostMapping("/blog/updateTop")
    public Result updateTop(Long id, Boolean top) {
        blogService.updateTop(id, top);
        return Result.success();
    }

    @PreAuthorize("hasAnyRole('admin')")
    @PostMapping("/blog/delete")
    @RemoveRedisCache(keyPattern = "blog*")
    @Transactional(rollbackFor = Exception.class)
    public Result deleteBlog(Long id) {
        blogService.removeById(id);
        return Result.success();
    }

    @GetMapping("/blog/get")
    public Result getBlogById(Long id) {
        BlogDetail blogDetail = blogService.getAdminBlogDetail(id);
        return Result.data(blogDetail);
    }

    @PreAuthorize("hasAnyRole('admin')")
    @PostMapping("/blog/update")
    public Result updateBlog(@RequestBody BlogDTO blogDTO) throws MyException {
        blogService.updateBlog(blogDTO);
        return Result.success();
    }

    @PreAuthorize("hasAnyRole('admin')")
    @PostMapping("/blog/save")
    public Result saveBlog(@RequestBody BlogDTO newBlog) {
        blogService.saveBlog(newBlog);
        return Result.success();
    }
}
