package com.touki.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.touki.blog.exception.MyException;
import com.touki.blog.model.dto.NewCategory;
import com.touki.blog.model.entity.Category;
import com.touki.blog.model.vo.PageResult;

/**
 * @author Touki
 */
public interface CategoryService extends IService<Category> {
    /**
     * 分页查询分类列表
     *
     * @param pageNum  分页索引
     * @param pageSize 分页大小
     * @return PageResult<Category>
     */
    PageResult<Category> categories(Integer pageNum, Integer pageSize);

    /**
     * 新建分类
     *
     * @param newCategory NewCategory
     */
    void saveCategory(NewCategory newCategory);

    /**
     * 删除分类
     *
     * @param categoryId 分类id
     */
    void deleteCategory(Long categoryId) throws MyException;
}
