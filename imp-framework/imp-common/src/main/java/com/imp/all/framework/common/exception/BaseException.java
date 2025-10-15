package com.imp.all.framework.common.exception;

import com.imp.all.framework.common.code.IErrorCode;

/**
 * @author Longlin
 * @date 2021/4/8 11:41
 * @description
 */
public class BaseException extends RuntimeException {

    private IErrorCode errorCode;

    public BaseException(IErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public IErrorCode getErrorCode() {
        return errorCode;
    }
}
