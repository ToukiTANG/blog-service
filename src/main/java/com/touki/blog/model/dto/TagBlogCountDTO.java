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
public class TagBlogCountDTO {
    private String name;// 标签名
    private Integer value;// 标签下博客数量
}
