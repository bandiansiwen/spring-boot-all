package com.imp.all.framework.security.core.exception;


import com.imp.all.framework.common.pojo.CommonResult;
import com.imp.all.framework.web.util.HttpResultUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Longlin
 * @date 2021/5/6 10:34
 * @description
 */
public class ImpVerificationFailedHandlerImpl implements ImpVerificationFailedHandler{
    @Override
    public void accessDenied(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException {
        HttpResultUtil.responseJson(httpServletResponse, CommonResult.forbidden(e.getMessage()));
    }

    @Override
    public void authenticationFailed(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
        HttpResultUtil.responseJson(httpServletResponse, CommonResult.unauthorized(e.getMessage()));
    }
}
