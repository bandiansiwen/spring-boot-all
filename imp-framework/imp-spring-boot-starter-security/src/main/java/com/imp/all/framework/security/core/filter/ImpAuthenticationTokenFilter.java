package com.imp.all.framework.security.core.filter;


import com.imp.all.framework.security.core.properties.ImpJwtProperties;
import com.imp.all.framework.security.core.utils.ImpJwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Longlin
 * @date 2021/3/30 10:58
 * @description
 */
public class ImpAuthenticationTokenFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImpAuthenticationTokenFilter.class);

    @Resource
    private UserDetailsService userDetailsService;
    @Resource
    private ImpJwtProperties impJwtProperties;
    @Resource
    private ImpJwtTokenUtil impJwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = httpServletRequest.getHeader(impJwtProperties.getTokenHeader());
        if (authHeader != null && authHeader.startsWith(impJwtProperties.getTokenPrefix())) {
            String authToken = authHeader.substring(impJwtProperties.getTokenPrefix().length());
            String userName = impJwtTokenUtil.getUserNameFromToken(authToken);
            LOGGER.info("checking username:{}", userName);
            if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
                if (impJwtTokenUtil.validateToken(authToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                    LOGGER.info("authenticated user:{}", userName);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
