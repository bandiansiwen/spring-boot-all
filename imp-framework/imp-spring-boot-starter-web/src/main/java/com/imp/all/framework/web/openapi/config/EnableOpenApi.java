package com.imp.all.framework.web.openapi.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author Longlin
 * @date 2023/1/16 16:59
 * @description
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(OpenApiConfig.class)
public @interface EnableOpenApi {
}
