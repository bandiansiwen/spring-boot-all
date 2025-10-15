package com.imp.all.framework.web.requestLimit.annotation;

/**
 * @author Longlin
 * @date 2023/7/11 15:34
 * @description
 */

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ApiLimit {
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
