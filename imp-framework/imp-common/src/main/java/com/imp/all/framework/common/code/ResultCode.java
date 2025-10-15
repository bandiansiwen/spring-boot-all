package com.imp.all.framework.common.code;

import lombok.Getter;

/**
 * @author Longlin
 * @date 2021/3/16 12:37
 * @description
 */
public enum ResultCode implements IErrorCode {

    SUCCESS(200, "成功"),

    // ========== 客户端错误段 ==========

    BAD_REQUEST(400, "参数检验失败"),
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    FORBIDDEN(403, "没有相关权限"),

    NOT_FOUND(404, "请求未找到"),

    METHOD_NOT_ALLOWED(405, "请求方法不正确"),

    LOCKED(423, "请求失败，请稍后重试"), // 并发请求，不允许

    TOO_MANY_REQUESTS(429, "请求过于频繁，请稍后重试"),

    // ========== 服务端错误段 ==========
    INTERNAL_SERVER_ERROR(500, "系统异常"),

    // ========== 自定义错误段 ==========

    REPEATED_REQUESTS(900, "重复请求，请稍后重试"), // 重复请求

    DEMO_DENY(901, "演示模式，禁止写操作"),

    UNKNOWN(999, "未知错误");

    @Getter
    private final Integer code;
    @Getter
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
