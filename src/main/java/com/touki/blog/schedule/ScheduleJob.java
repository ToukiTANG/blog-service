package com.touki.blog.schedule;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @author Touki
 */
@Data
public class ScheduleJob {
    public static final String JOB_PARAM_KEY = "JOB_PARAM_KEY";
    @TableId
    private Long jobId;
    private String beanName;
    private String methodName;
    private String params;
    private String cron;
    private Boolean status;
    private String remark;
    private Date createTime = new Date();
    private Date updateTime = new Date();
}
