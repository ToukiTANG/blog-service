package com.touki.blog.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @author Touki
 */
@Data
public class Tag {
    @TableId
    private Long tagId;
    private String tagName;
    private Date createTime;
    private Date updateTime;
}
