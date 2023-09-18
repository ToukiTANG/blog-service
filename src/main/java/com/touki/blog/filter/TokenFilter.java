package com.touki.blog.filter;

import com.touki.blog.constant.DelimiterConstant;
import com.touki.blog.constant.EndpointConstant;
import com.touki.blog.constant.RespCode;
import com.touki.blog.model.vo.Result;
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

                    List<GrantedAuthority> authorityList =
                            AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(username, null, authorityList);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request, response);
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
