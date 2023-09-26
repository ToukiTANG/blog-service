package com.touki.blog.model.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author Touki
 */
@Data
public class CategoryUpdate {
    private Long categoryId;
    private String categoryName;
    private Date createTime;
    private Date updateTime;
}
