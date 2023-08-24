package com.touki.blog.model.vo;

import com.touki.blog.model.entity.Category;
import com.touki.blog.model.entity.Tag;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author Touki
 * 首页文章信息
 */
@Data
public class BlogInfo {
    private Long blogId;
    private String title;
    private String firstPicture;
    private String description;
    private Date createTime;
    private Date updateTime;
    private Integer views;
    private Boolean top;
    private Boolean commentEnable;
    private Category category;
    private List<Tag> tags;
}
