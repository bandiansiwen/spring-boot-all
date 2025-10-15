package com.imp.all.framework.security.core.exception;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Longlin
 * @date 2021/3/30 10:29
 * @description 无权限处理类
 */
public class ImpAccessDeniedHandler implements AccessDeniedHandler {

    @Resource
    ImpVerificationFailedHandler impVerificationFailedHandler;

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        impVerificationFailedHandler.accessDenied(httpServletRequest, httpServletResponse, e);
    }
}
