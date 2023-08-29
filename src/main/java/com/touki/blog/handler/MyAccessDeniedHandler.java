package com.touki.blog.handler;

import com.touki.blog.constant.RespCode;
import com.touki.blog.model.vo.Result;
import com.touki.blog.util.NetworkUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Touki
 */
@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        NetworkUtil.setJsonResponse(response, Result.message(RespCode.FORBIDDEN, "无权限访问！"));
    }
}
