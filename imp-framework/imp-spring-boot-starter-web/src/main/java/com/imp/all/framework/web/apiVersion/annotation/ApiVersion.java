package com.imp.all.framework.web.apiVersion.annotation;

import java.lang.annotation.*;

/**
 * @author Longlin
 * @date 2021/4/22 17:51
 * @description
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiVersion {
    /**
     * 标识版本号
     */
    String value();
}
