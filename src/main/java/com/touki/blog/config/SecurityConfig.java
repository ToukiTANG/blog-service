package com.touki.blog.config;

import com.touki.blog.constant.EndpointConstant;
import com.touki.blog.filter.LoginFilter;
import com.touki.blog.service.impl.SysUserServiceImpl;
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
    private SysUserServiceImpl sysUserService;

    public SecurityConfig(SysUserServiceImpl sysUserService) {
        this.sysUserService = sysUserService;
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
                .authorizeRequests().anyRequest().authenticated().and()
                .addFilterAt(loginFilter(http), UsernamePasswordAuthenticationFilter.class)
        ;
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public LoginFilter loginFilter(HttpSecurity http) throws Exception {
        LoginFilter loginFilter = new LoginFilter();
        loginFilter.setAuthenticationManager(authenticationManager(http));
        loginFilter.setFilterProcessesUrl("/login");
        return loginFilter;
    }
}
