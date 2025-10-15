package com.imp.all.framework.web.apiVersion.annotation;

import com.imp.all.framework.web.apiVersion.config.ApiAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author Longlin
 * @date 2021/4/22 17:54
 * @description 开启接口的多版本支持 不需要了
 */
@Target(ElementType.TYPE)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Import(ApiAutoConfiguration.class)
public @interface EnableApiVersion {
}
