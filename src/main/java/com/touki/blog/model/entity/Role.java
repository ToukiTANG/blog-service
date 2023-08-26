package com.touki.blog.model.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 角色表
 * role
 */
@Data
public class Role implements Serializable {
    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 角色名
     */
    private String roleName;

    private static final long serialVersionUID = 1L;
}