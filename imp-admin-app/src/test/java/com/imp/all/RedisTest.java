package com.imp.all;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisTest {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    RedissonClient redissonClient;

    @Test
    public void redisson() {
        System.out.println(redissonClient);
    }

    @Test
    public void set() {
        redisTemplate.opsForValue().set("test:set1", "testValue1");
        redisTemplate.opsForValue().set("test:set2", "testValue2");
        redisTemplate.opsForSet().add("set2", "set3Value");
        redisTemplate.opsForHash().put("hash2", "aaa", "bbb");
        System.out.println(redisTemplate.opsForValue().get("test:set1"));
        System.out.println(redisTemplate.opsForValue().get("test:set2"));
        System.out.println(redisTemplate.opsForHash().get("hash2", "aaa"));
    }
}