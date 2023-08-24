package com.touki.blog.model.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author Touki
 */
@Data
public class RandomBlog {
    private Long blogId;
    private String title;
    private String firstPicture;
    private Date createTime;
}
