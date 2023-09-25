package com.touki.blog.model.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author Touki
 */
@Data
public class MomentUpdate {
    private Long momentId;
    private String content;
    private Integer likes;
    private Boolean published;
    private Date createTime;
    private Date updateTime = new Date();
}
