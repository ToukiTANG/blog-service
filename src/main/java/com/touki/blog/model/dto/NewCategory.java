package com.touki.blog.model.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author Touki
 */
@Data
public class NewCategory {
    private String categoryName;
    private Date createTime = new Date();
    private Date updateTime = new Date();
}
