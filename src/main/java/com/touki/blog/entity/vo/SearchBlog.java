package com.touki.blog.entity.vo;

import lombok.Data;

/**
 * @author Touki
 */
@Data
public class SearchBlog {
    private Long blogId;
    private String title;
    private String contentText;
}
