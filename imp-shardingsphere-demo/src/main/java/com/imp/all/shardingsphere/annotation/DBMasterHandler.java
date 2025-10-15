package com.imp.all.shardingsphere.annotation;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.infra.hint.HintManager;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@Aspect
public class DBMasterHandler {

    @Around("@annotation(dbMaster)")
    public Object master(ProceedingJoinPoint joinPoint, DBMaster dbMaster){

        Object ret = null;

        HintManager hintManager = null;
        try {
            if (Objects.nonNull(dbMaster)) {
                HintManager.clear();
                hintManager = HintManager.getInstance();
                hintManager.setWriteRouteOnly();
            }
            ret = joinPoint.proceed();
        }catch (Exception ex){
            log.error("exception error", ex);
        }catch (Throwable ex2){
            log.error("Throwable",ex2);
        }finally {
            if (Objects.nonNull(dbMaster) && Objects.nonNull(hintManager)) {
                hintManager.close();
            }
        }
        return ret;
    }
}