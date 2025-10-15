package com.imp.all.framework.common.exception;

import com.imp.all.framework.common.code.IErrorCode;

/**
 * @author Longlin
 * @date 2021/4/13 14:45
 * @description
 */
public class Asserts {
    public static void fail(String message) {
        throw new BaseException(message);
    }

    public static void fail(IErrorCode errorCode) {
        throw new BaseException(errorCode);
    }
}
