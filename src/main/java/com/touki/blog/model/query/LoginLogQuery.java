package com.touki.blog.model.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Touki
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LoginLogQuery extends BaseQuery {
    String date;
}
