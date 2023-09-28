package com.touki.blog.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.touki.blog.constant.RespCode;
import com.touki.blog.exception.MyException;
import com.touki.blog.mapper.BlogMapper;
import com.touki.blog.mapper.CategoryMapper;
import com.touki.blog.model.dto.NewCategory;
import com.touki.blog.model.entity.Blog;
import com.touki.blog.model.entity.Category;
import com.touki.blog.model.vo.PageResult;
import com.touki.blog.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Touki
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    private final BlogMapper blogMapper;

    public CategoryServiceImpl(BlogMapper blogMapper) {
        this.blogMapper = blogMapper;
    }

    @Override
    public PageResult<Category> categories(Integer pageNum, Integer pageSize) {
        Page<Category> categoryPage = new Page<>(pageNum, pageSize);
        categoryPage = this.page(categoryPage);

        PageResult<Category> pageResult = new PageResult<>();
        pageResult.setPageSize(pageSize);
        pageResult.setTotal((int) categoryPage.getTotal());
        pageResult.setDataList(categoryPage.getRecords());
        return pageResult;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveCategory(NewCategory newCategory) {
        Category category = new Category();
        BeanUtils.copyProperties(newCategory, category);
        this.save(category);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCategory(Long categoryId) throws MyException {
        Blog blog = blogMapper.existBlogInCategory(categoryId);
        if (blog != null) {
            throw new MyException(RespCode.PARAMETER_ERROR, "该分类下还有文章，不允许删除！");
        }
        this.removeById(categoryId);
    }
}
