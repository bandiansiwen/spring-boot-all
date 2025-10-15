package com.imp.all.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.imp.all.entity.AdminUser;
import com.imp.all.entity.AdminUserValidate;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

/**
 * @author Longlin
 * @date 2021/3/16 23:52
 * @description
 */
public interface UserService {

    List<AdminUser> getAllUsers();

    Page<AdminUser> getUserList(Integer currentPage, Integer pageSize);

    UserDetails loadUserByUsername(String username);

    AdminUserValidate registerUser(AdminUser adminUser);

    AdminUserValidate login(String username, String password);
}
