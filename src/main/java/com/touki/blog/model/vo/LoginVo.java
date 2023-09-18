package com.touki.blog.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Touki
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginVo {
    private String accessToken;
    private UserVo user;
}
