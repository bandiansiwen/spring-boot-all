package com.imp.all.framework.web.requestLimit.annotation;

import java.lang.annotation.*;

/**
 * @author Longlin
 * @date 2021/5/17 16:55
 * @description 依赖 redis，可实现分布式限流
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RequestLimit {
    /**
     * 资源的名字
     *
     * @return String
     */
    String name() default "";

    /**
     * 资源的key
     *
     * @return String
     */
    String key() default "";

    /**
     * Key的prefix
     *
     * @return String
     */
    String prefix() default "";

    /**
     * 给定的时间段
     * 单位秒
     * 限流key过期时间
     * @return int
     */
    int period() default 1;

    /**
     *
     * 允许访问的次数，默认值MAX_VALUE
     */
    int count() default Integer.MAX_VALUE;

    /**
     * 类型
     *
     * @return LimitType
     */
    LimitType limitType() default LimitType.IP;

    /**
     *
     * 限流默认返回提示语句
     */
    String message() default "系统繁忙,请稍后再试.";
}
