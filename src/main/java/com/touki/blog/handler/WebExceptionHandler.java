package com.touki.blog.handler;

import com.touki.blog.constant.RespCode;
import com.touki.blog.entity.vo.Result;
import com.touki.blog.exception.MyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Touki
 */
@Slf4j
@RestControllerAdvice
public class WebExceptionHandler {
    @ExceptionHandler(Exception.class)
    public Result exceptionHandle(Exception e) {
        log.error(e.getMessage());
        return Result.message(RespCode.SERVER_ERROR, "服务器异常！");
    }

    @ExceptionHandler(MyException.class)
    public Result myExceptionHandle(MyException e) {
        log.error(e.getMsg());
        return Result.message(e.getReturnCode(), e.getMsg());
    }
}
