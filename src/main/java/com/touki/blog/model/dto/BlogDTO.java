package com.touki.blog.model.dto;

import com.touki.blog.model.vo.ContentInfo;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author Touki
 */
@Data
public class BlogDTO {
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

    // 这里因为前端有可能传来的是category（新加）字符串或者id（已存在），因此用object接受并在业务逻辑进行判断
    private Object cate;
    private Long categoryId;

    private List<Object> tagList;
}
