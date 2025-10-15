package com.imp.all.framework.web.requestLimit.aspect;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.ImmutableList;
import com.imp.all.framework.common.pojo.CommonResult;
import com.imp.all.framework.web.requestLimit.annotation.LimitType;
import com.imp.all.framework.web.requestLimit.annotation.RequestLimit;
import com.imp.all.framework.web.util.HttpRequestUtil;
import com.imp.all.framework.web.util.HttpResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @author Longlin
 * @date 2021/5/18 17:03
 * @description
 */
@Slf4j
@Aspect
public class RequestLimitAspect {

    /**
     * getRedisScript 读取脚本工具类
     * 这里设置为Long,是因为ipLimiter.lua 脚本返回的是数字类型
     */
    private DefaultRedisScript<Long> getRedisScript;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Pointcut("@annotation(com.imp.all.framework.web.requestLimit.annotation.RequestLimit)")
    public void requestLimit() {

    }

    @Around("requestLimit()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取request,response
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        // 或者url(存在map集合的key)
        String url = request.getRequestURI();
        // 获取自定义注解
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        //使用Java 反射技术获取方法上是否有@CustomRateLimiter 注解类
        RequestLimit requestLimit = signature.getMethod().getDeclaredAnnotation(RequestLimit.class);
        if (requestLimit != null) {
            LimitType limitType = requestLimit.limitType();
            String name = requestLimit.name();
            String prefix = requestLimit.prefix();
            String key = null;
            int limitPeriod = requestLimit.period();
            int limitCount = requestLimit.count();
            if (limitType == LimitType.URL) {
                key = url;
            }
            else if (limitType == LimitType.IP) {
                key = HttpRequestUtil.getRequestIp(request);
            }
            else if (limitType == LimitType.CUSTOMER) {
                key = requestLimit.key();
            }
            ImmutableList<String> keys = ImmutableList.of(StrUtil.join(prefix, key));
            // 拿redis全局锁
            String luaScript = buildLuaScript();
            RedisScript<Long> redisScript = new DefaultRedisScript<>(luaScript, Long.class);
            Long count = (Long) redisTemplate.execute(redisScript, keys, limitCount, limitPeriod);
            Long result = (Long) redisTemplate.execute(getRedisScript, keys, limitPeriod, limitCount);
            log.info("Access try count is {} for name={} and key = {}", count, name, key);
            if (count == null || count.intValue() > limitCount) {
                //服务降级
                assert response != null;
                HttpResultUtil.responseJson(response, CommonResult.failed(requestLimit.message()));
                return null;
            }
        }
        //正常执行方法,执行正常业务逻辑
        return joinPoint.proceed();
    }

    /**
     * 限流 脚本
     * @return lua脚本
     */
    private String buildLuaScript() {
        StringBuilder lua = new StringBuilder();
        lua.append("local c");
        lua.append("\nc = redis.call('get',KEYS[1])");
        // 调用不超过最大值，则直接返回
        lua.append("\nif c and tonumber(c) > tonumber(ARGV[1]) then");
        lua.append("\nreturn c;");
        lua.append("\nend");
        // 执行计算器自加
        lua.append("\nc = redis.call('incr',KEYS[1])");
        lua.append("\nif tonumber(c) == 1 then");
        // 从第一次调用开始限流，设置对应键值的过期
        lua.append("\nredis.call('expire',KEYS[1],ARGV[2])");
        lua.append("\nend");
        lua.append("\nreturn c;");
        return lua.toString();
    }

    @PostConstruct
    public void init() {
        getRedisScript = new DefaultRedisScript<>();
        getRedisScript.setResultType(Long.class);
        getRedisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("ipLimiter.lua")));
        log.info("IpLimitHandler[分布式限流处理器]脚本加载完成");
    }
}
