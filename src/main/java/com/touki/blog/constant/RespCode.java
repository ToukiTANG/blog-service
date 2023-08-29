package com.touki.blog.constant;

/**
 * @author Touki
 */

public abstract class RespCode {
    public static final Integer SUCCESS = 2000;
    public static final Integer PARAMETER_ERROR = 2001;
    public static final Integer AUTHENTICATE_FAIL = 4001;
    public static final Integer EXPIRED_JWT = 4002;
    public static final Integer FORBIDDEN = 4003;
    public static final Integer SERVER_ERROR = 5000;
}
