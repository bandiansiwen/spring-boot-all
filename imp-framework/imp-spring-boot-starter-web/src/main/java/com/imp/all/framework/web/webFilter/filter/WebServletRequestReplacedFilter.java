package com.imp.all.framework.web.webFilter.filter;


import com.imp.all.framework.web.util.ServletUtils;
import com.imp.all.framework.web.webFilter.wrapper.WebHttpServletRequestWrapper;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Longlin
 * @date 2021/4/23 17:17
 * @description
 */
//public class WebServletRequestReplacedFilter implements Filter {
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        ServletRequest requestWrapper = null;
//         // post 请求/不是上传请求
//        if (servletRequest instanceof HttpServletRequest
//                && HttpMethod.POST.matches(((HttpServletRequest) servletRequest).getMethod())
//                && !ServletFileUpload.isMultipartContent((HttpServletRequest) servletRequest)) {
//            requestWrapper = new WebHttpServletRequestWrapper((HttpServletRequest) servletRequest);
//        }
//        if (requestWrapper == null) {
//            filterChain.doFilter(servletRequest, servletResponse);
//        }
//        else {
//            filterChain.doFilter(requestWrapper, servletResponse);
//        }
//    }
//}

public class WebServletRequestReplacedFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        filterChain.doFilter(new WebHttpServletRequestWrapper(request), response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // 只处理 json 请求内容
        return !ServletUtils.isJsonRequest(request);
    }

}
