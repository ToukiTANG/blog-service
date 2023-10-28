package com.touki.blog.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Touki
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VisitorUpdateDTO {
    private String uuid;
    private Date time;
    private Integer pv;
}
