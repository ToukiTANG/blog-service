package com.touki.blog.model.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Touki
 */
@Data
public class Role implements Serializable {
    private Long roleId;
    private String roleName;
}