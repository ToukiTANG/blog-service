package com.touki.blog.model.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author Touki
 */
@Data
public class VisitLogStatistic {
    private String uuid;
    private Date time;
    private Integer pv;
}
