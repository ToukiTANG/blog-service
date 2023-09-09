package com.touki.blog.model.vo;

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
    private ContentInfo content;
    private String description;
    private Date createTime;
    private Date updateTime;
    private Integer views;
    private Boolean top;
    private Boolean commentEnable;
    private CategoryInfo category;
    private List<TagInfo> tags;
}
