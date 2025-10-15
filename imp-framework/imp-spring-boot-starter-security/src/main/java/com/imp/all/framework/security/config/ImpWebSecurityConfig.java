package com.imp.all.framework.security.config;

import com.imp.all.framework.security.core.dynamic.ImpDynamicSecurityFilter;
import com.imp.all.framework.security.core.dynamic.ImpDynamicSecurityService;
import com.imp.all.framework.security.core.exception.ImpAccessDeniedHandler;
import com.imp.all.framework.security.core.exception.ImpAuthenticationEntryPoint;
import com.imp.all.framework.security.core.exception.ImpVerificationFailedHandler;
import com.imp.all.framework.security.core.exception.ImpVerificationFailedHandlerImpl;
import com.imp.all.framework.security.core.filter.ImpAuthenticationTokenFilter;
import com.imp.all.framework.security.core.properties.ImpIgnoreUrlsProperties;
import com.imp.all.framework.security.core.properties.ImpJwtProperties;
import com.imp.all.framework.security.core.utils.ImpJwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

/**
 * @author Longlin
 * @date 2021/3/22 21:47
 * @description
 * prePostEnabled = true 会解锁 @PreAuthorize 和 @PostAuthorize 两个注解
 * prePostEnabled = true 会解锁 @Secured注解是用来定义业务方法的安全配置
 * jsr250Enabled = true  启用 JSR-250 安全控制注解 @DenyAll @RolesAllowed @PermitAll
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true, prePostEnabled = true, securedEnabled = true)
@EnableConfigurationProperties({ImpJwtProperties.class, ImpIgnoreUrlsProperties.class})
public class ImpWebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired(required = false)
    private ImpDynamicSecurityService impDynamicSecurityService;

    @Resource
    ImpIgnoreUrlsProperties impIgnoreUrlsProperties;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // <1> 访问 EndPoint 地址，需要经过认证，并且拥有 ADMIN 角色   actuator接口
//        http.requestMatcher(EndpointRequest.toAnyEndpoint()).authorizeRequests((requests) ->
//                requests.anyRequest().hasRole("ADMIN"));

        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http.authorizeRequests();
        //不需要保护的资源路径允许访问
        for (String url : impIgnoreUrlsProperties.getUrls()) {
            registry.antMatchers(url).permitAll();
        }
        //允许跨域请求的OPTIONS请求
        registry.antMatchers(HttpMethod.OPTIONS)
                .permitAll();
        // 任何请求需要身份认证
        registry.anyRequest().authenticated()
                //启用CORS支持
                .and().cors()
                // 关闭跨站请求防护及不使用session
                .and().csrf().disable()
                //禁止生成Session,防止内存溢出
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                // 自定义权限拒绝处理类
                .and().exceptionHandling()
                .accessDeniedHandler(impAccessDeniedHandler())
                .authenticationEntryPoint(impAuthenticationEntryPoint())
                // 自定义权限拦截器JWT过滤器
                .and().addFilterBefore(impAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        // 有动态权限配置时添加动态权限校验过滤器
        if (impDynamicSecurityService != null) {
            registry.and().addFilterBefore(impDynamicSecurityFilter(), FilterSecurityInterceptor.class);
        }
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    // 装载BCrypt密码编码器
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 权限不足处理类
    @Bean
    ImpAccessDeniedHandler impAccessDeniedHandler() {
        return new ImpAccessDeniedHandler();
    }

    // 认证失败处理类
    @Bean
    ImpAuthenticationEntryPoint impAuthenticationEntryPoint() {
        return new ImpAuthenticationEntryPoint();
    }

    @ConditionalOnMissingBean(ImpVerificationFailedHandler.class)
    @Bean
    ImpVerificationFailedHandler impVerificationFailedHandler() {
        return new ImpVerificationFailedHandlerImpl();
    }

    // token过滤器来验证token有效性
    @Bean
    ImpAuthenticationTokenFilter impAuthenticationTokenFilter() {
        return new ImpAuthenticationTokenFilter();
    }

    // token生成与解析
    @Bean
    ImpJwtTokenUtil impJwtTokenUtil() {
        return new ImpJwtTokenUtil();
    }

    // 动态过滤器
    @ConditionalOnBean(name = "impDynamicSecurityService")
    @Bean
    public ImpDynamicSecurityFilter impDynamicSecurityFilter() {
        return new ImpDynamicSecurityFilter();
    }
}
