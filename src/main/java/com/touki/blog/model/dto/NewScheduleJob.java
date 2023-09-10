package com.touki.blog.model.dto;

import lombok.Data;

/**
 * @author Touki
 */
@Data
public class NewScheduleJob {
    private String beanName;
    private String methodName;
    private String params;
    private String cron;
    private Boolean status;
    private String remark;
}
