package com.touki.blog.entity;

import lombok.Data;

/**
 * @author Touki
 */
@Data
public class SiteSetting {
    private Long id;
    private String nameZh;
    private String nameEn;
    private String value;
    // 类型，1基础设置，2资料设置
    private Integer type;
}
