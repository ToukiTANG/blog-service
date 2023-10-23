package com.touki.blog.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Touki
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAgentDTO {
    private String os;
    private String browser;
}
