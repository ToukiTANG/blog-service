package com.touki.blog.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author Touki
 */
@Data
public class About {
    @TableId
    private Long aboutId;
    private String nameEn;
    private String nameZh;
    private String value;
}
