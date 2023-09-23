package com.touki.blog.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author Touki
 */
@Data
public class SiteSetting {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String nameZh;
    private String nameEn;
    private String value;
    // 类型，1基础设置，2资料设置
    private Integer type;
}
