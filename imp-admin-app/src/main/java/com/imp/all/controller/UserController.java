package com.imp.all.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.imp.all.dao.UserMapper;
import com.imp.all.entity.AdminUser;
import com.imp.all.entity.AdminUserValidate;
import com.imp.all.framework.common.pojo.CommonResult;
import com.imp.all.framework.security.core.utils.ImpJwtTokenUtil;
import com.imp.all.framework.web.log.annotation.HiddenField;
import com.imp.all.framework.web.log.annotation.HiddenMode;
import com.imp.all.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Longlin
 * @date 2021/3/16 23:49
 * @description
 */
@RestController
@RequestMapping("/user")
@Api(value = "UserController", tags = "后台用户管理")
public class UserController {

    @Resource
    private UserService userService;

    @Value("${imp.jwt.tokenPrefix}")
    private String tokenPrefix;

    @Value("${imp.jwt.tokenHeader}")
    private String tokenHeader;

    @Resource
    private ImpJwtTokenUtil jwtTokenUtil;

    @Resource
    private UserMapper userMapper;

    @ApiOperation(value = "用户注册")
    @PostMapping("/register")
    public CommonResult<?> registerUser(@Validated @RequestBody AdminUser adminUser) {
        AdminUserValidate userValidate = userService.registerUser(adminUser);
        if (userValidate.getIsSuccess()) {
            Map<String, String> tokenMap = new HashMap<>();
            tokenMap.put("token", userValidate.getToken());
            tokenMap.put("tokenHead", tokenPrefix);
            tokenMap.put("tokenHeader", tokenHeader);
            return CommonResult.success(tokenMap);
        }
        return CommonResult.failed(userValidate.getMessage());
    }

    @ApiOperation(value = "用户注册")
    @PostMapping("/insertUser")
    public CommonResult<?> insertUser(@RequestBody AdminUser adminUser, @RequestParam String paramStr) {
        userMapper.insert(adminUser);
        return CommonResult.success();
    }

    @ApiOperation(value = "用户注册")
    @GetMapping("/insertUser")
    public CommonResult<?> insertUser(@RequestParam String username, @RequestParam @HiddenField(type = HiddenMode.EMAIL, endIndex = "3") String password) {
        return CommonResult.success();
    }

    @ApiOperation(value = "用户注册")
    @PostMapping("/deleteAll")
    public CommonResult<?> deleteAll() {
//        userMapper.deleteAll();
        return CommonResult.success();
    }

    @ApiOperation(value = "用户登录")
    @PostMapping("/login")
    public CommonResult<?> login(@Validated @RequestBody AdminUser adminUser) {
        AdminUserValidate userValidate = userService.login(adminUser.getUsername(), adminUser.getPassword());
        if (userValidate.getIsSuccess()) {
            Map<String, String> tokenMap = new HashMap<>();
            tokenMap.put("token", userValidate.getToken());
            tokenMap.put("tokenHead", tokenPrefix);
            tokenMap.put("tokenHeader", tokenHeader);
            return CommonResult.success(tokenMap);
        }
        return CommonResult.failed(userValidate.getMessage());
    }

    @ApiOperation(value = "刷新token")
    @PostMapping("/refreshToken")
    public CommonResult<?> refreshToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String refreshToken = jwtTokenUtil.refreshToken(token);
        if (refreshToken == null) {
            return CommonResult.failed("token已经过期！");
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", refreshToken);
        tokenMap.put("tokenHead", tokenPrefix);
        return CommonResult.success(tokenMap);
    }

    @ApiOperation(value = "获取所有用户列表")
    @GetMapping("/list")
    public CommonResult<List<AdminUser>> getAllUsers() {
        List<AdminUser> adminUsers = userService.getAllUsers();
        return CommonResult.success(adminUsers);
    }

    @ApiOperation(value = "获取用户列表-分页")
    @GetMapping("/list/{currentPage}/{pageSize}")
    public CommonResult<Page<AdminUser>> getUserList(@PathVariable int currentPage, @PathVariable int pageSize) {
        Page<AdminUser> adminUsers = userService.getUserList(currentPage, pageSize);
        return CommonResult.success(adminUsers);
    }

    @ApiOperation(value = "根据名称获取用户")
    @GetMapping("/{userName}")
    public CommonResult<AdminUser> getUserByName(@PathVariable String userName) {
        AdminUser user = userMapper.getUserByName(userName);
        return CommonResult.success(user);
    }

    @ApiOperation(value = "修改用户昵称")
    @PostMapping("/update")
    public CommonResult<?> setUserNickName(AdminUser user) {
        int i = userMapper.updateUserNickName(user.getUsername(), user.getNickName());
        return CommonResult.success(i);
    }

    @ApiOperation(value = "新建用户")
    @PostMapping("/insert")
    // 批量操作 可删除多个缓存
    @Caching(evict = {
            @CacheEvict(value = {"allUsers"}, key = "'getAllUsers'")
    })
//    @CacheEvict(value = {"allUsers"}, allEntries = true)  删除所有的entries
    public CommonResult<?> createUser(AdminUser user) {
        int i = userMapper.insertUser(user.getUsername(), user.getPassword(), user.getUsername() + "222", user.getPassword()+ "222");
        return CommonResult.success(i);
    }
}
