package com.touki.blog.model.dto;

import lombok.Data;

/**
 * @author Touki
 */
@Data
public class BlogTagDto {
    private Long blogId;
    private Long tagId;
    private String tagName;
}
