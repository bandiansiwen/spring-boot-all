package com.imp.all.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Longlin
 * @date 2021/4/14 16:25
 * @description
 */
@Setter
@Getter
public class AdminUserValidate {

    private Boolean isSuccess;
    private String message;
    private String token;

    protected AdminUserValidate(String token, Boolean isSuccess, String message) {
        this.token = token;
        this.isSuccess = isSuccess;
        this.message = message;
    }

    public static AdminUserValidate failed(String message) {
        return new AdminUserValidate(null, false, message);
    }

    public static AdminUserValidate success(String token) {
        return new AdminUserValidate(token, true, null);
    }
}
