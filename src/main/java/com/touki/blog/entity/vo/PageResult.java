package com.touki.blog.entity.vo;

import lombok.Data;

import java.util.List;

/**
 * @author Touki
 * 分页结果
 */
@Data
public class PageResult<T> {
    private Integer pageSize;
    private Integer total;
    private List<T> dataList;
}
