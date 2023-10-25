package com.touki.blog.enums;

import lombok.Getter;

/**
 * @author Touki
 */
@Getter
public enum VisitBehaviorEnum {
    UNKNOWN("UNKNOWN", "UNKNOWN"),

    INDEX("访问页面", "首页"),
    ARCHIVE("访问页面", "归档"),
    MOMENT("访问页面", "动态"),
    ABOUT("访问页面", "关于我"),

    BLOG("查看博客", ""),
    CATEGORY("查看分类", ""),
    TAG("查看标签", ""),
    SEARCH("搜索博客", ""),
    LIKE_MOMENT("点赞动态", ""),
    ;
    /**
     * 访问行为
     */
    private final String behavior;
    /**
     * 访问内容
     */
    private final String content;

    VisitBehaviorEnum(String behavior, String content) {
        this.behavior = behavior;
        this.content = content;
    }
}
