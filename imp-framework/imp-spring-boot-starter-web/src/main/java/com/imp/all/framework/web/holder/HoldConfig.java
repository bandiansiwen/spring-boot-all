package com.imp.all.framework.web.holder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Longlin
 * @date 2022/1/25 11:06
 * @description
 */

@Configuration
public class HoldConfig {

    @Bean
    public ApplicationContextHolder applicationContextHolder() {
        return new ApplicationContextHolder();
    }
}
