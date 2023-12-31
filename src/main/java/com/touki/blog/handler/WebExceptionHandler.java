package com.touki.blog.handler;

import com.touki.blog.constant.RespCode;
import com.touki.blog.exception.MyException;
import com.touki.blog.model.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
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
        log.error("\n********************内部错误********************", e);
        return Result.message(RespCode.SERVER_ERROR, "服务器异常！");
    }

    @ExceptionHandler(MyException.class)
    public Result myExceptionHandle(MyException e) {
        log.error("自定义异常！", e);
        return Result.message(e.getReturnCode(), e.getMsg());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public Result accessDeniedExceptionHandle(AccessDeniedException e) {
        log.error("权限异常！", e);
        return Result.message(RespCode.FORBIDDEN, "无权访问！");
    }
}
