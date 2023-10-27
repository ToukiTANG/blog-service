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
public class VisitLogRemarkDTO {
    /**
     * 访问内容
     */
    private String content;

    /**
     * 备注
     */
    private String remark;
}
