package com.imp.all.framework.web.requestLimit.aspect;

import cn.hutool.core.util.StrUtil;
import com.imp.all.framework.common.pojo.CommonResult;
import com.imp.all.framework.web.requestLimit.annotation.ApiLimit;
import com.imp.all.framework.web.requestLimit.annotation.LimitType;
import com.imp.all.framework.web.util.HttpRequestUtil;
import com.imp.all.framework.web.util.HttpResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @author Longlin
 * @date 2023/7/11 15:33
 * @description
 */
@Slf4j
@Aspect
public class ApiLimitAspect {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Pointcut("@annotation(com.imp.all.framework.web.requestLimit.annotation.ApiLimit)")
    public void apiLimit() {

    }

    @Around("apiLimit()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        // 获取request,response
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        // 或者url(存在map集合的key)
        String url = request.getRequestURI();
        // 获取自定义注解
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        //使用Java 反射技术获取方法上是否有@CustomRateLimiter 注解类
        ApiLimit apiLimit = signature.getMethod().getDeclaredAnnotation(ApiLimit.class);

        if (apiLimit != null) {
            LimitType limitType = apiLimit.limitType();
            String name = apiLimit.name();
            String prefix = apiLimit.prefix();
            String key = null;
            int limitPeriod = apiLimit.period();
            int limitCount = apiLimit.count();
            if (limitType == LimitType.URL) {
                key = url;
            }
            else if (limitType == LimitType.IP) {
                key = HttpRequestUtil.getRequestIp(request);
            }
            else if (limitType == LimitType.CUSTOMER) {
                key = apiLimit.key();
            }
            String redisKey = StringUtils.hasLength(name) ? name : StrUtil.join(prefix, key);
            if (StringUtils.hasLength(redisKey)) {

                long cur = System.currentTimeMillis();
                long start = cur - limitPeriod* 1000L;
                // 添加访问请求
                redisTemplate.opsForZSet().add(redisKey, cur, cur);
                // 移除滑动窗口之外的数据
                redisTemplate.opsForZSet().removeRangeByScore(redisKey, 0, start);
                // 获取窗口内的数量
                Long count = redisTemplate.opsForZSet().count(redisKey, start, cur);

                if (count==null || count>limitCount) {
                    //服务降级
                    assert response != null;
                    HttpResultUtil.responseJson(response, CommonResult.failed(apiLimit.message()));
                    return null;
                }
            }
            else {
                throw new RuntimeException("ApiLimit限流key不能为空");
            }
        }

        //正常执行方法,执行正常业务逻辑
        return joinPoint.proceed();
    }
}


