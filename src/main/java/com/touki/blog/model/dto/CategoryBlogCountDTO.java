package com.touki.blog.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Touki
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryBlogCountDTO {
    private String name;// 分类名
    private Integer value = 0;// 分类下博客数量
}
