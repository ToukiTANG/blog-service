package com.touki.blog.model.query;

import lombok.Data;

/**
 * @author Touki
 */
@Data
public abstract class BaseQuery {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
