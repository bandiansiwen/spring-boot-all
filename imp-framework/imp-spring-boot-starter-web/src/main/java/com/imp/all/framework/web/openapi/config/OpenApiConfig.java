package com.imp.all.framework.web.openapi.config;

import com.imp.all.framework.web.openapi.aop.OpenApiAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Longlin
 * @date 2023/1/16 16:57
 * @description
 */
@Configuration(proxyBeanMethods = false)
public class OpenApiConfig {

    @Bean
    public OpenApiAspect openApiAspect() {
        return new OpenApiAspect();
    }
}
