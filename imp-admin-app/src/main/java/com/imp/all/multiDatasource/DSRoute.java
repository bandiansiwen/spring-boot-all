package com.imp.all.multiDatasource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Longlin
 * @date 2021/9/10 17:02
 * @description 用于指定数据库
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DSRoute {

    DBTypeEnum value() default DBTypeEnum.MASTER;
}
