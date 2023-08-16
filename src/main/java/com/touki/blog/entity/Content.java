package com.touki.blog.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @author Touki
 */
@Data
public class Content {
    @TableId
    private Long contentId;
    private String text;
    private Date createTime;
    private Date updateTime;
}
