package com.touki.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.touki.blog.entity.Category;
import com.touki.blog.mapper.CategoryMapper;
import com.touki.blog.service.CategoryService;
import org.springframework.stereotype.Service;

/**
 * @author Touki
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
}
