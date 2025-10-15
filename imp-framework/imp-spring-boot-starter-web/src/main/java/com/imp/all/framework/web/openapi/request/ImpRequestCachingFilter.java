package com.imp.all.framework.web.openapi.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Longlin
 * @date 2023/1/16 16:40
 * @description
 */
@Slf4j
public class ImpRequestCachingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        boolean isFirstRequest = !isAsyncDispatch(request);
        HttpServletRequest requestToUse = request;
        if (isFirstRequest && !(request instanceof BufferedHttpServletRequest)) {
            requestToUse = new BufferedHttpServletRequest(request);
        }
        try {
            filterChain.doFilter(requestToUse, response);
        } catch (Exception e) {
            log.error("ImpRequestCachingFilter>>>>>>>>>", e);
        }
    }
}
