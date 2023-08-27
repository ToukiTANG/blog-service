package com.touki.blog.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Touki
 */
@Data
public class Role implements Serializable {
    @TableId
    private Long roleId;
    private String roleName;
}