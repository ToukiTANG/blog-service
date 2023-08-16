package com.touki.blog.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @author Touki
 */
@Data
public class Tag {
    @TableId
    private Integer tagId;
    private String tagName;
    private String description;
    private Date createTime;
    private Date updateTime;
}
