package com.touki.blog.model.vo;

import com.touki.blog.model.entity.Role;
import lombok.Data;

import java.util.List;

/**
 * @author Touki
 */
@Data
public class UserVo {
    private Long userId;
    private String username;
    private String nickname;
    private String avatar;
    private List<Role> roles;
}
