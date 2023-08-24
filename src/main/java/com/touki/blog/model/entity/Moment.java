package com.touki.blog.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @author Touki
 */
@Data
public class Moment {
    @TableId
    private Long momentId;

    private String content;

    private Date createTime;

    private Integer likes;
}
