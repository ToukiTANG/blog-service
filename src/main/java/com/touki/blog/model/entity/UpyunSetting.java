package com.touki.blog.model.entity;

import lombok.Data;

/**
 * @author Touki
 */
@Data
public class UpyunSetting {
    private String bucketName;
    private String username;
    private String password;
    private String baseUrl;
}
