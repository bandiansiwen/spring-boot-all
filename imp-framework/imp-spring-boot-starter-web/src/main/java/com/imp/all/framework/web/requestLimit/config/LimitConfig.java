package com.imp.all.framework.web.requestLimit.config;


import com.imp.all.framework.web.requestLimit.aspect.ApiLimitAspect;
import com.imp.all.framework.web.requestLimit.aspect.RateLimitAspect;
import com.imp.all.framework.web.requestLimit.aspect.RequestLimitAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Longlin
 * @date 2021/5/18 10:10
 * @description
 */
@Configuration
public class LimitConfig {

    @Bean
    public RateLimitAspect rateLimitAspect() {
        return new RateLimitAspect();
    }

    @Bean
    public RequestLimitAspect requestLimitAspect() {
        return new RequestLimitAspect();
    }

    @Bean
    public ApiLimitAspect apiLimitAspect() {
        return new ApiLimitAspect();
    }
}
