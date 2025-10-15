package com.imp.all.framework.web.requestLimit.aspect;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.RateLimiter;
import com.imp.all.framework.common.pojo.CommonResult;
import com.imp.all.framework.web.requestLimit.annotation.RateLimit;
import com.imp.all.framework.web.util.HttpResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;

/**
 * @author Longlin
 * @date 2021/5/18 10:10
 * @description
 *
 * Spring4.x
 * 正常情况：环绕前置=====@Before======目标方法执行=====环绕返回=====环绕最终=====@After=====@AfterReturning
 * 异常情况：环绕前置=====@Before======目标方法执行=====@After=====@AfterThrowing
 * Spring5.x
 * 正常情况：环绕前置=====@Before=====目标方法执行=====@AfterReturning=====@After=====环绕返回=====环绕最终
 * 异常情况：环绕前置=====@Before=====目标方法执行=====@AfterThrowing=====@After
 */
@Slf4j
@Aspect
public class RateLimitAspect {
    /**
     * 使用url做为key,存放令牌桶 防止每次重新创建令牌桶
     */
    private Map<String, RateLimiter> limitMap = Maps.newConcurrentMap();

    @Pointcut("@annotation(com.imp.all.framework.web.requestLimit.annotation.RateLimit)")
    public void rateLimit() {

    }

    @Around("rateLimit()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取request,response
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        // 或者url(存在map集合的key)
        String url = request.getRequestURI();
        // 获取自定义注解
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        //使用Java 反射技术获取方法上是否有@CustomRateLimiter 注解类
        RateLimit rateLimit = signature.getMethod().getDeclaredAnnotation(RateLimit.class);
        if (rateLimit != null) {
            RateLimiter limit;
            // 判断map集合中是否有创建有创建好的令牌桶
            if (!limitMap.containsKey(url)) {
                // 创建令牌桶
                synchronized (signature.getMethod()) {
                    if (limitMap.containsKey(url)) {
                        limit = limitMap.get(url);
                    }
                    else {
                        limit = RateLimiter.create(rateLimit.permitsPerSecond());
                        limitMap.put(url, limit);
                        log.info("<<=================  请求{},创建令牌桶,容量{} 成功!!!", url, rateLimit.permitsPerSecond());
                    }
                }
            }
            else {
                limit = limitMap.get(url);
            }
            // 获取令牌
            boolean acquire = limit.tryAcquire(rateLimit.timeout(), rateLimit.timeunit());
            if (!acquire) {
                //服务降级
                assert response != null;
                HttpResultUtil.responseJson(response, CommonResult.failed(rateLimit.msg()));
                return null;
            }
        }
        //正常执行方法,执行正常业务逻辑
        return joinPoint.proceed();
    }
}
