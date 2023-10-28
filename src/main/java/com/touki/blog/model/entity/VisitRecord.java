package com.touki.blog.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
public class VisitRecord {
    @TableId(type = IdType.ASSIGN_ID)
    private Long recordId;
    // 访问量
    private Integer pv;
    // 独立访问，类似于新用户
    private Integer uv;
    private String date;

    public VisitRecord(Integer pv, Integer uv, String date) {
        this.pv = pv;
        this.uv = uv;
        this.date = date;
    }
}
