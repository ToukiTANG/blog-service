package com.touki.blog.model.vo;

import lombok.Data;

/**
 * @author Touki
 */
@Data
public class ArchiveBlog {
    private String yearMonth;

    private Long blogId;

    private String title;
    // 当月第几天的博客
    private String day;
}
