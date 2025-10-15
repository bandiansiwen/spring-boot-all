package com.imp.all.controller;

import com.imp.all.service.IdempotentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author Longlin
 * @date 2021/4/23 15:13
 * @description 接口幂等性
 * 类似于前端重复提交、重复下单、没有唯一ID号的场景，可以通过 Token 与 Redis 配合的“防重 Token 方案”实现更为快捷。
 */
@Slf4j
@RestController
@RequestMapping("/idempotent")
@Api(value = "IdempotentController", tags = "接口幂等性")
public class IdempotentController {

    @Resource
    private IdempotentService idempotentService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 获取 Token 接口
     * @return Token 串
     */
    @GetMapping("/token")
    @ApiOperation(value = "获取幂等 Token 接口")
    public String getToken() {
        // 获取用户信息（这里使用模拟数据）
        // 注：这里存储该内容只是举例，其作用为辅助验证，使其验证逻辑更安全，如这里存储用户信息，其目的为:
        // - 1)、使用"token"验证 Redis 中是否存在对应的 Key
        // - 2)、使用"用户信息"验证 Redis 的 Value 是否匹配。
        String userInfo = "my-user-info";
        // 获取 Token 字符串，并返回
        return idempotentService.generateIdempotentToken(userInfo);
    }

    @GetMapping("/transaction")
    @ApiOperation(value = "redis 事务")
    public String testTransaction() {

        // 采用 RedisTemplate 的默认配置，即不开启事务支持。可以通过使用 SessionCallback，该接口保证其内部所有操作都是在同一个Session中。
        // 6.x之后的写法
        SessionCallback<Object> sessionCallback = new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {

//                redisTemplate.opsForValue().set("key1", 1, 10, TimeUnit.SECONDS);
//                operations.watch("key1");
                operations.multi();
                // 执行 redis 操作
                redisTemplate.opsForValue().set("key2", "v2", 60, TimeUnit.SECONDS);
                //v2值应为null
                Object v2 = redisTemplate.opsForValue().get("key2");
                log.info("命令在队列，所以v2为:" + v2);
                redisTemplate.opsForValue().set("key3", "v3", 60, TimeUnit.SECONDS);
                //v3值应为null
                Object v3 = redisTemplate.opsForValue().get("key3");
                log.info("命令在队列，所以v3为:" + v3);
//                redisTemplate.opsForValue().increment("key1",1); //①
                return operations.exec();
            }
        };
        Object res = redisTemplate.execute(sessionCallback);
        log.info(Objects.requireNonNull(res).toString());

        Object v2 = redisTemplate.opsForValue().get("key2");
        Object v3 = redisTemplate.opsForValue().get("key3");
        log.info("结果：" + v2 + "-" + v3);

        return "test transaction";
    }

    /**
     * 接口幂等性测试接口
     *
     * @param token 幂等 Token 串
     * @return 执行结果
     */
    @PostMapping("/test")
    @ApiOperation(value = "测试幂等接口")
    public String test(@RequestHeader(value = "token") String token) {
        // 获取用户信息（这里使用模拟数据）
        String userInfo = "my-user-info";
        // 根据 Token 和与用户相关的信息到 Redis 验证是否存在对应的信息
        boolean result = idempotentService.validIdempotentToken(token, userInfo);
        // 根据验证结果响应不同信息
        return result ? "正常调用" : "重复调用";
    }
}

