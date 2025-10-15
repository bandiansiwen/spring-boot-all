package com.imp.all.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.imp.all.dao.UserMapper;
import com.imp.all.entity.AdminUser;
import com.imp.all.entity.AdminUserDetails;
import com.imp.all.entity.AdminUserValidate;
import com.imp.all.framework.common.exception.Asserts;
import com.imp.all.framework.security.core.utils.ImpJwtTokenUtil;
import com.imp.all.service.UserCacheService;
import com.imp.all.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Longlin
 * @date 2021/3/16 23:52
 * @description
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Resource
    private ImpJwtTokenUtil impJwtTokenUtil;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserCacheService userCacheService;

    @Cacheable(value = {"allUsers"}, key = "#root.method.name")  // 当前方法结果缓存
    @Override
    public List<AdminUser> getAllUsers() {
        return userMapper.getAllUsers();
    }

    @Override
    public Page<AdminUser> getUserList(Integer currentPage, Integer pageSize) {
        Page<AdminUser> page = new Page<>(currentPage, pageSize);
        return userMapper.selectPage(page, null);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        AdminUser user = userCacheService.getUser(username);
        if (user == null) {
            user = userMapper.getUserByName(username);
            if (user == null) {
                // 空结果缓存：解决缓存穿透
                AdminUser adminUser = new AdminUser();
                adminUser.setUsername(username);
                user = adminUser;
            }
            userCacheService.setUser(user);
        }
        return new AdminUserDetails(user);
    }

    @Override
    public AdminUserValidate registerUser(AdminUser adminUser) {
        // 查询是否有相同用户名
        AdminUser user = userMapper.getUserByName(adminUser.getUsername());
        if (user != null) {
            return AdminUserValidate.failed("用户名已存在");
        }
        adminUser.setStatus(1);
        String encodedPassword = passwordEncoder.encode(adminUser.getPassword());
        adminUser.setPassword(encodedPassword);
        userMapper.insert(adminUser);
        String generateToken = impJwtTokenUtil.generateToken(new AdminUserDetails(adminUser));
        return AdminUserValidate.success(generateToken);
    }

    @Override
    public AdminUserValidate login(String username, String password) {
        String token;
        //密码需要客户端加密后传递
        try {
            UserDetails userDetails = loadUserByUsername(username);
            if(!passwordEncoder.matches(password,userDetails.getPassword())){
                Asserts.fail("密码不正确");
            }
            if(!userDetails.isEnabled()){
                Asserts.fail("帐号已被禁用");
            }
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            token = impJwtTokenUtil.generateToken(userDetails);
        } catch (AuthenticationException e) {
            return AdminUserValidate.failed("登录异常:" + e.getMessage());
        }
        return AdminUserValidate.success(token);
    }
}
