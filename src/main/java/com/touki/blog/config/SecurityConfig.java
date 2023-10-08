package com.touki.blog.config;

import com.touki.blog.constant.EndpointConstant;
import com.touki.blog.filter.LoginFilter;
import com.touki.blog.filter.TokenFilter;
import com.touki.blog.handler.MyAccessDeniedHandler;
import com.touki.blog.handler.MyAuthenticationEntryPoint;
import com.touki.blog.service.impl.SysUserServiceImpl;
import com.touki.blog.util.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author Touki
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtUtil jwtUtil;
    private final SysUserServiceImpl sysUserService;
    private final MyAccessDeniedHandler accessDeniedHandler;
    private final MyAuthenticationEntryPoint authenticationEntryPoint;

    public SecurityConfig(JwtUtil jwtUtil, SysUserServiceImpl sysUserService, MyAccessDeniedHandler accessDeniedHandler,
                          MyAuthenticationEntryPoint authenticationEntryPoint) {
        this.jwtUtil = jwtUtil;
        this.sysUserService = sysUserService;
        this.accessDeniedHandler = accessDeniedHandler;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.
                getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(sysUserService)
                .and()
                .build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable().cors().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .requestMatchers().antMatchers(EndpointConstant.ADMIN).and()
                .authorizeRequests().antMatchers(EndpointConstant.ADMIN_LOGIN).permitAll().anyRequest().authenticated().and()
                .addFilterAt(loginFilter(http), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(tokenFilter(), LoginFilter.class)
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler).authenticationEntryPoint(authenticationEntryPoint)
        ;
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public LoginFilter loginFilter(HttpSecurity http) throws Exception {
        LoginFilter loginFilter = new LoginFilter(jwtUtil);
        loginFilter.setAuthenticationManager(authenticationManager(http));
        loginFilter.setFilterProcessesUrl(EndpointConstant.ADMIN_LOGIN);
        return loginFilter;
    }

    @Bean
    public TokenFilter tokenFilter() {
        return new TokenFilter(jwtUtil);
    }
}
