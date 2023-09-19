package com.touki.blog.filter;

import com.touki.blog.constant.RespCode;
import com.touki.blog.model.dto.AuthUser;
import com.touki.blog.model.entity.SysUser;
import com.touki.blog.model.vo.LoginVo;
import com.touki.blog.model.vo.Result;
import com.touki.blog.model.vo.UserVo;
import com.touki.blog.util.JsonUtil;
import com.touki.blog.util.JwtUtil;
import com.touki.blog.util.NetworkUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Touki
 */
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!"POST".equals(request.getMethod())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        try {
            SysUser user = JsonUtil.readValue(request.getInputStream(), SysUser.class);
            if (user != null) {
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
                setDetails(request, authenticationToken);
                return getAuthenticationManager().authenticate(authenticationToken);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException {
        AuthUser authUser = (AuthUser) authResult.getPrincipal();
        String accessToken = JwtUtil.createAccessToken(authUser);
        UserVo user = new UserVo();
        BeanUtils.copyProperties(authUser, user);
        LoginVo loginVo = new LoginVo(accessToken, user);
        NetworkUtil.setJsonResponse(response, Result.data(loginVo));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {
        if (failed instanceof BadCredentialsException) {
            NetworkUtil.setJsonResponse(response, Result.message(RespCode.AUTHENTICATE_FAIL, "用户名或密码错误！"));
        } else if (failed instanceof AccountStatusException) {
            NetworkUtil.setJsonResponse(response, Result.message(RespCode.AUTHENTICATE_FAIL, "用户已被禁用，请联系管理员！"));
        } else {
            NetworkUtil.setJsonResponse(response, Result.message(RespCode.AUTHENTICATE_FAIL, "用户已被禁用，请联系管理员！"));
        }
    }
}
