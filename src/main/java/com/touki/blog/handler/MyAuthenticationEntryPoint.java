package com.touki.blog.handler;

import com.touki.blog.constant.RespCode;
import com.touki.blog.model.vo.Result;
import com.touki.blog.util.NetworkUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Touki
 */
@Slf4j
@Component
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        log.info(authException.getMessage());
        NetworkUtil.setJsonResponse(response, Result.message(RespCode.AUTHENTICATE_FAIL, "认证失败！"));
    }
}
