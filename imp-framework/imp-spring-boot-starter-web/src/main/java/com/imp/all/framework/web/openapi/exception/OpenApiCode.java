package com.imp.all.framework.web.openapi.exception;

import com.imp.all.framework.common.code.IErrorCode;
import lombok.Getter;

/**
 * @author Longlin
 * @date 2023/1/16 16:52
 * @description
 */
public enum OpenApiCode implements IErrorCode {

    SYSKEY_ERROR(27001, "SYSKEY_ERROR"),
    SGIN_ERROR(27002, "SGIN_ERROR"),
    TIMESTAMP_ERROR(27003, "TIMESTAMP_ERROR"),
    ACCESSAUTH_ERROR(27004, "ACCESS_AUTH_ERROR");

    @Getter
    private final Integer code;
    @Getter
    private final String message;

    OpenApiCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
