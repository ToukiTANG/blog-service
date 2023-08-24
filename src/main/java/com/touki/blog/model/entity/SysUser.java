package com.touki.blog.model.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author Touki
 */
@Data
public class SysUser {
    private Long userId;
    private String username;
    private String password;
    private String nickname;
    private String avatar;
    private String email;
    private Date createTime;
    private Date updateTime;
}
