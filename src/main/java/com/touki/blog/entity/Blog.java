package com.touki.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;


/**
 * @author Touki
 */
@Data
public class Blog {
    @TableId(type = IdType.ASSIGN_ID)
    private Long blogId;
    private String title;
    private String firstPicture;
    private Long contentId;
    private String description;
    private Date createTime;
    private Date updateTime;
    private Integer views;
    private Boolean top;
    private Boolean commentEnable;
    private Long categoryId;
}
