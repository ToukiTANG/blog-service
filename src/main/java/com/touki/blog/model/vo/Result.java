package com.touki.blog.model.vo;

import com.touki.blog.constant.RespCode;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Touki
 */
@Data
@AllArgsConstructor
public class Result {
    private Integer code;
    private String msg;
    private Object data;

    public static Result success() {
        return new Result(RespCode.SUCCESS, "success", null);
    }

    public static Result message(Integer code, String msg) {
        return new Result(code, msg, null);
    }

    public static Result data(Object data) {
        return new Result(RespCode.SUCCESS, "success", data);
    }
}
