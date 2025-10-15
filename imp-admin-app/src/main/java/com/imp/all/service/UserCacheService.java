package com.imp.all.service;

import com.imp.all.entity.AdminUser;

/**
 * @author Longlin
 * @date 2021/4/22 11:46
 * @description 缓存用户数据，做对比使用，非登录用户数据
 */
public interface UserCacheService {

    /**
     * 删除后台用户缓存
     */
    void delUser(Long adminId);
    /**
     * 获取缓存后台用户信息
     */
    AdminUser getUser(String username);

    /**
     * 设置缓存后台用户信息
     */
    void setUser(AdminUser user);
}
