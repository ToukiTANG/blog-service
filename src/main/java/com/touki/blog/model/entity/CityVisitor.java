package com.touki.blog.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Touki
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CityVisitor {
    @TableId
    private String city;// 城市名称
    private Integer uv;// 独立访客数量
}
