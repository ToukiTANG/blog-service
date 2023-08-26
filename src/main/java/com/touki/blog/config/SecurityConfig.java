package com.touki.blog.config;

import com.touki.blog.service.impl.SysUserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;

/**
 * @author Touki
 */
@Configuration
public class SecurityConfig {
    private final SysUserServiceImpl sysUserService;

    public SecurityConfig(SysUserServiceImpl sysUserService) {
        this.sysUserService = sysUserService;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        List<AuthenticationProvider> providerList = Collections.singletonList(daoAuthenticationProvider());
        return new ProviderManager(providerList);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(sysUserService);
        return provider;
    }
}
