package com.touki.blog.model.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author Touki
 */
@Data
public class NewTag {
    private String tagName;
    private Date createTime = new Date();
    private Date updateTime = new Date();
}
