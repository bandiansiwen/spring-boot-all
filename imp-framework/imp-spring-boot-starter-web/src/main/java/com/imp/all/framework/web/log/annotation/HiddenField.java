package com.imp.all.framework.web.log.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Longlin
 * @date 2022/8/26 17:55
 * @description
 * 注解的保留策略有三种：SOURCE/ClASS/RUNTIME
 */

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface HiddenField {

    HiddenMode type() default HiddenMode.PASSWORD;

    String startIndex() default "";
    String endIndex() default "";
}
