package com.touki.blog.exception;

import com.touki.blog.constant.RespCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Touki
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MyException extends Exception {
    private int returnCode = RespCode.SERVER_ERROR;
    private String msg;

    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public MyException(int returnCode, String msg) {
        this.returnCode = returnCode;
        this.msg = msg;
    }
}
