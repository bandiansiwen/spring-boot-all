package com.imp.all.service.impl;

import cn.hutool.core.lang.UUID;
import com.imp.all.service.IdempotentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Longlin
 * @date 2021/4/23 15:15
 * @description
 */
@Service
@Slf4j
public class IdempotentServiceImpl implements IdempotentService {
    /**
     * 存入 Redis 的 Token 键的前缀
     */
    private static final String IDEMPOTENT_TOKEN_PREFIX = "idempotent_token:";

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 创建Token存入Redis, 并返回Token
     * @param value
     * @return
     */
    public String generateIdempotentToken(String value) {
        // 实例化生成 ID 工具对象
        String token = UUID.randomUUID().toString();
        // 设置存入 Redis 的 Key
        String key = IDEMPOTENT_TOKEN_PREFIX + token;
        // 存储 Token 到 Redis，且设置过期时间为5分钟
        redisTemplate.opsForValue().set(key, value, 5000);
        // 返回 Token
        return token;
    }

    /**
     * 验证 Token 正确性
     * @param token
     * @param value
     * @return
     */
    public boolean validIdempotentToken(String token, String value) {
        String key = IDEMPOTENT_TOKEN_PREFIX + token;
        String val = (String) redisTemplate.opsForValue().get(key);
        if (val != null && val.equals(value)) {
            log.info("验证 token={},key={},value={} 成功", token, key, value);
            redisTemplate.delete(key);
            return true;
        }
        log.info("验证 token={},key={},value={} 失败", token, key, value);
        return false;
    }
}
