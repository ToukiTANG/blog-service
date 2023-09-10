package com.touki.blog.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @author Touki
 */
@Data
public class ScheduleJobLog {
    @TableId
    private Long logId;
    private Long jobId;
    private String beanName;
    private String methodName;
    private String params;
    private Boolean status;
    private String errorMsg;
    private Integer timeConsumed;
    private Date createTime;
}
