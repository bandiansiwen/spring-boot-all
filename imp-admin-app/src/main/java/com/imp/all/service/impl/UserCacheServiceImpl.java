package com.imp.all.service.impl;

import com.imp.all.dao.UserMapper;
import com.imp.all.entity.AdminUser;
import com.imp.all.service.UserCacheService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Longlin
 * @date 2021/4/22 13:50
 * @description
 */
@Service
public class UserCacheServiceImpl implements UserCacheService {

    @Value("${redis.database}")
    private String REDIS_DATABASE;
    @Value("${redis.expire.common}")
    private Long REDIS_EXPIRE;
    @Value("${redis.key.admin}")
    private String REDIS_KEY_ADMIN;

    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void delUser(Long adminId) {
        AdminUser user = userMapper.selectById(adminId);
        if (user != null) {
            String key = REDIS_DATABASE + ":" + REDIS_KEY_ADMIN + ":" + user.getUsername();
            redisTemplate.delete(key);
        }
    }

    @Override
    public AdminUser getUser(String username) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_ADMIN + ":" + username;
        return (AdminUser) redisTemplate.opsForValue().get(key);
    }

    @Override
    public void setUser(AdminUser user) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_ADMIN + ":" + user.getUsername();
        redisTemplate.opsForValue().set(key, user, REDIS_EXPIRE);
    }
}
