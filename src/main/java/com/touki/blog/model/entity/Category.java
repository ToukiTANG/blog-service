package com.touki.blog.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @author Touki
 */
@Data
public class Category {
    @TableId
    private Long categoryId;
    private String categoryName;
    private String description;
    private Date createTime;
    private Date updateTime;
}
