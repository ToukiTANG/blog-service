package com.touki.blog.controller.admin;

import com.touki.blog.annotation.RemoveRedisCache;
import com.touki.blog.constant.RedisKeyConstant;
import com.touki.blog.exception.MyException;
import com.touki.blog.model.dto.NewCategory;
import com.touki.blog.model.entity.Category;
import com.touki.blog.model.query.SimpleQuery;
import com.touki.blog.model.vo.PageResult;
import com.touki.blog.model.vo.Result;
import com.touki.blog.service.CategoryService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author Touki
 */
@RestController
@RequestMapping("/admin/category")
public class AdminCategoryController {
    private final CategoryService categoryService;

    public AdminCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/list")
    public Result categories(SimpleQuery query) {
        PageResult<Category> categoryPageResult = categoryService.categories(query.getPageNum(), query.getPageSize());
        return Result.data(categoryPageResult);
    }

    @PreAuthorize("hasAnyRole('admin')")
    @PostMapping("/save")
    public Result saveCategory(@RequestBody NewCategory newCategory) {
        categoryService.saveCategory(newCategory);
        return Result.success();
    }

    @PreAuthorize("hasAnyRole('admin')")
    @PostMapping("/delete")
    public Result deleteCategory(Long id) throws MyException {
        categoryService.deleteCategory(id);
        return Result.success();
    }

    @PreAuthorize("hasAnyRole('admin')")
    @PostMapping("/update")
    @RemoveRedisCache(key = RedisKeyConstant.SITE_SETTING_INFO)
    @Transactional(rollbackFor = Exception.class)
    public Result updateCategory(Category category) {
        category.setUpdateTime(new Date());
        categoryService.updateById(category);
        return Result.success();
    }
}
