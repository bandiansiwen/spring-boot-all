package com.imp.all.framework.web.requestLimit.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Longlin
 * @date 2021/5/18 10:06
 * @description
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RateLimit {

    //往令牌桶放入令牌的速率
    double permitsPerSecond();

    //获取令牌最大等待时间
    long timeout() default 0;

    // 单位(例:分钟/秒/毫秒) 默认:毫秒
    TimeUnit timeunit() default TimeUnit.MILLISECONDS;

    // 无法获取令牌返回提示信息 默认值可以自行修改
    String msg() default "系统繁忙,请稍后再试.";
}
