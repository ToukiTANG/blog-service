package com.touki.blog.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @author Touki
 */
@Data
public class Category {
    @TableId
    private Integer categoryId;
    private String categoryName;
    private String description;
    private Date createTime;
    private Date updateTime;
}
