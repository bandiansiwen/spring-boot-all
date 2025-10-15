package com.imp.all.framework.web.openapi.sign.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

/**
 * @author Longlin
 * @date 2023/1/16 15:49
 * @description
 */
@Target({ METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OpenApiSignBodyMethod {
}
