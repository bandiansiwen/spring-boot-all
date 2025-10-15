package com.imp.all.advice;

import java.lang.annotation.*;

/**
 * @author Longlin
 * @date 2022/10/18 11:31
 * @description
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ImpEncode {
}
