package com.touki.blog.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @author Touki
 */
@Data
public class SysUser {
    @TableId
    private Long userId;
    private String username;
    private String password;
    private String nickname;
    private String avatar;
    private String email;
    private Boolean enable;
    private Date createTime;
    private Date updateTime;
}
