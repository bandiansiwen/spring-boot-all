package com.imp.all.config;

import com.imp.all.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.annotation.Resource;

/**
 * @author Longlin
 * @date 2021/3/31 15:43
 * @description
 */
@Configuration
public class AdminSecurityConfig {

    @Resource
    UserService userService;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userService.loadUserByUsername(username);
    }
}
