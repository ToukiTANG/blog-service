package com.touki.blog.filter;

import com.touki.blog.constant.DelimiterConstant;
import com.touki.blog.constant.EndpointConstant;
import com.touki.blog.constant.RespCode;
import com.touki.blog.constant.TokenType;
import com.touki.blog.model.dto.AuthUser;
import com.touki.blog.model.vo.Result;
import com.touki.blog.model.vo.TokenVo;
import com.touki.blog.service.impl.SysUserServiceImpl;
import com.touki.blog.util.JwtUtil;
import com.touki.blog.util.NetworkUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author Touki
 */
public class TokenFilter extends OncePerRequestFilter {
    private final SysUserServiceImpl sysUserService;

    public TokenFilter(SysUserServiceImpl sysUserService) {
        this.sysUserService = sysUserService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().contains(EndpointConstant.ADMIN_LOGIN) || !request.getRequestURI().startsWith(
                "/admin")) {
            filterChain.doFilter(request, response);
        } else {
            String header = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (!StringUtils.isBlank(header) && (header.startsWith("bearer ") || header.startsWith("Bearer "))) {
                String username;
                String authorities;
                try {
                    String token = StringUtils.split(header, DelimiterConstant.SPACE)[1];
                    Claims claims = JwtUtil.parseJwt(token);
                    username = claims.getSubject();
                    authorities = claims.get("authorities", String.class);
                    String type = claims.get("type", String.class);

                    if (TokenType.ACCESS_TOKEN.equals(type)) {
                        List<GrantedAuthority> authorityList =
                                AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(username, null, authorityList);
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        filterChain.doFilter(request, response);
                    } else if (TokenType.REFRESH_TOKEN.equals(type)) {
                        AuthUser authUser = (AuthUser) sysUserService.loadUserByUsername(username);
                        String accessToken = JwtUtil.createAccessToken(authUser);
                        String refreshToken = JwtUtil.createRefreshToken(authUser);
                        TokenVo tokenVo = new TokenVo(accessToken, refreshToken);
                        NetworkUtil.setJsonResponse(response, Result.data(tokenVo));
                    }
                } catch (MalformedJwtException e) {
                    NetworkUtil.setJsonResponse(response, Result.message(RespCode.AUTHENTICATE_FAIL, "非法token，认证失败！"));
                } catch (ExpiredJwtException e) {
                    NetworkUtil.setJsonResponse(response, Result.message(RespCode.EXPIRED_JWT, "token已过期！"));
                }
            } else {
                NetworkUtil.setJsonResponse(response, Result.message(RespCode.AUTHENTICATE_FAIL, "错误格式的token！认证失败！"));
            }
        }
    }
}
