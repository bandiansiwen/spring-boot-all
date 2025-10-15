package com.imp.all.framework.security.core.exception;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Longlin
 * @date 2021/3/30 10:34
 * @description 未认证处理类
 */
public class ImpAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Resource
    ImpVerificationFailedHandler impVerificationFailedHandler;

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        impVerificationFailedHandler.authenticationFailed(httpServletRequest, httpServletResponse, e);

    }
}
