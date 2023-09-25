package com.touki.blog.model.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author Touki
 */
@Data
public class NewMoment {
    private String content;

    private Integer likes;

    private Boolean published;

    private Date createTime = new Date();
}
