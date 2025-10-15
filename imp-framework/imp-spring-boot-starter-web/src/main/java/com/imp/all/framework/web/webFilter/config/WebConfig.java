package com.imp.all.framework.web.webFilter.config;

import com.imp.all.framework.common.enums.WebFilterOrderEnum;
import com.imp.all.framework.web.webFilter.filter.WebServletRequestReplacedFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

/**
 * @author Longlin
 * @date 2021/4/23 16:33
 * @description
 */
@Configuration(proxyBeanMethods = false)
public class WebConfig {

    /**
     * 创建 RequestBodyCacheFilter Bean，可重复读取请求内容
     */
    @Bean
    public FilterRegistrationBean<WebServletRequestReplacedFilter> requestBodyCacheFilter() {
        return createFilterBean(new WebServletRequestReplacedFilter(), WebFilterOrderEnum.REQUEST_BODY_CACHE_FILTER);
    }

    private static <T extends Filter> FilterRegistrationBean<T> createFilterBean(T filter, Integer order) {
        FilterRegistrationBean<T> bean = new FilterRegistrationBean<>(filter);
        bean.setOrder(order);
        return bean;
    }
}
