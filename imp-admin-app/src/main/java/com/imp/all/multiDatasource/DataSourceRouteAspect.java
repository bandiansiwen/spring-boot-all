package com.imp.all.multiDatasource;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * @author Longlin
 * @date 2021/9/10 17:03
 * @description 注解指定某个数据源
 */
@Aspect
@Slf4j
public class DataSourceRouteAspect {

    @Around("@annotation(dsRoute)")
    public Object aroundDataSourceRoute(ProceedingJoinPoint joinPoint, DSRoute dsRoute) throws Throwable {
        DBTypeEnum originDbTypeEnum = DataSourceContextHolder.getDBType();
        log.warn("设置 dbTypeEnum {} into DataSourceContext", dsRoute.value());
        DataSourceContextHolder.annotationSet(dsRoute.value());
        try {
            return joinPoint.proceed();
        } finally {
            if (originDbTypeEnum != null) {
                // 注解执行完之后需要把原来的数据源还原
                DataSourceContextHolder.reSet(originDbTypeEnum);
            }
            else {
                DataSourceContextHolder.clear();
            }
        }
    }
}
