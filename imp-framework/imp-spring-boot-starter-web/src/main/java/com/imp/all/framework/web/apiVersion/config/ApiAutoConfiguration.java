package com.imp.all.framework.web.apiVersion.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * @author Longlin
 * @date 2021/4/22 17:55
 * @description
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(ApiVersionProperties.class)
@ConditionalOnProperty(value = "imp.api-version.enable", havingValue = "true")
public class ApiAutoConfiguration implements WebMvcRegistrations {

    @Override
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        return new ApiHandlerMapping();
    }
}
