package com.touki.blog.entity.vo;

import com.touki.blog.entity.Category;
import com.touki.blog.entity.Content;
import com.touki.blog.entity.Tag;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author Touki
 */
@Data
public class BlogDetail {
    private Long blogId;
    private String title;
    private String firstPicture;
    private Content content;
    private String description;
    private Date createTime;
    private Date updateTime;
    private Integer views;
    private Boolean top;
    private Boolean commentEnable;
    private Category category;
    private List<Tag> tags;
}
