package com.touki.blog.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Touki
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MyException extends Exception {
    private String msg;
}
