package com.imp.all.multiDatasource;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * @author Longlin
 * @date 2021/9/10 17:14
 * @description
 */
@Aspect
@Slf4j
public class ReadWriteSeparateAspect {

//    @Pointcut("!@annotation(com.imp.all.multiDatasource.DSRoute)" +
//            "&& (execution(* com.imp.all..*.select*(..))" +
//            "|| execution(* com.imp.all..*.get*(..)))")
//    public void readPointcut() {
//
//    }
//
//    @Pointcut("!@annotation(com.imp.all.multiDatasource.DSRoute)" +
//            "&& (execution(* com.imp.all..*.insert*(..))" +
//            "|| execution(* com.imp.all..*.add*(..))" +
//            "|| execution(* com.imp.all..*.update*(..))" +
//            "|| execution(* com.imp.all..*.edit*(..))" +
//            "|| execution(* com.imp.all..*.delete*(..))" +
//            "|| execution(* com.imp.all..*.remove*(..)))")
//    public void writePointcut() {
//
//    }
//
//    @Before("readPointcut()")
//    public void read() {
//        DataSourceContextHolder.slave();
//    }
//
//    @Before("writePointcut()")
//    public void write() {
//        DataSourceContextHolder.master();
//    }

    /**
     * 另一种写法：if...else...  判断哪些需要读从数据库，其余的走主数据库
     */
    @Before("!@annotation(com.imp.all.multiDatasource.DSRoute) && execution(* com.imp.all.dao..*(..))")
    public void before(JoinPoint jp) {
        String methodName = jp.getSignature().getName();

        if (StrUtil.containsAny(methodName, "get", "select", "find")) {
            DataSourceContextHolder.slave();
        }else {
            DataSourceContextHolder.master();
        }
    }
}
