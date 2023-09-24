package com.touki.blog.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author Touki
 */
@Data
public class Content {
    @TableId
    private Long contentId;
    private String text;
}
