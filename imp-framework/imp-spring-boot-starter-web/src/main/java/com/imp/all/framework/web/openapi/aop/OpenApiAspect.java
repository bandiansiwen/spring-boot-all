package com.imp.all.framework.web.openapi.aop;

import com.imp.all.framework.common.exception.BaseException;
import com.imp.all.framework.web.openapi.exception.OpenApiCode;
import com.imp.all.framework.web.openapi.model.OpenApiModel;
import com.imp.all.framework.web.openapi.service.OpenApiConfigService;
import com.imp.all.framework.web.openapi.sign.SignValidator;
import com.imp.all.framework.web.openapi.sign.annotations.OpenApiSignBodyMethod;
import com.imp.all.framework.web.openapi.sign.annotations.OpenApiSignMethod;
import com.imp.all.framework.web.util.HttpRequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Longlin
 * @date 2023/1/16 16:56
 * @description
 */
@Slf4j
@Aspect
public class OpenApiAspect {

    /**
     * 时间戳超时时间（秒）
     */
    @Value("${common.openapi.timestampSecLimit:1800}")
    private int timestampLimit;

    @Autowired(required = false)
    @Lazy
    private OpenApiConfigService openApiConfigService;

    public OpenApiAspect() {
        log.info(">> enable OpenApiAspectAop");
    }

    @Pointcut("@annotation(com.imp.all.framework.web.openapi.sign.annotations.OpenApiSignMethod)")
    public void signMethod() {
    }

    @Pointcut("@annotation(com.imp.all.framework.web.openapi.sign.annotations.OpenApiSignBodyMethod)")
    public void signBodyMethod() {
    }

    @Before(value = "signMethod() || signBodyMethod()")
    public void doBefore(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            OpenApiSignMethod openApiSignMethod = method.getAnnotation(OpenApiSignMethod.class);
            OpenApiSignBodyMethod openApiSignBodyMethod = method.getAnnotation(OpenApiSignBodyMethod.class);

            Map<String, Object> params = HttpRequestUtil.getParameterMapContent(request);
            if (openApiSignMethod != null) {
                String sign = params.get("sign") == null ? null : String.valueOf(params.get("sign"));
                //需要计算签名的参数，不含 sign 字段
                params.remove("sign");
                doValidate(params, sign);
            }
            else if (openApiSignBodyMethod != null) {

                String body = HttpRequestUtil.getBodyContent(request);

                String sign = params.get("sign") == null ? null : String.valueOf(params.get("sign"));
                //需要计算签名的参数，不含 sign 字段
                params.remove("sign");

                doValidate(body, params, sign);
            }
        }
    }

    private boolean checkAccessAuth(String sysKey) {
        List<String> accessUrl = openApiConfigService.getAccessUrl(sysKey);
        if (CollectionUtils.isEmpty(accessUrl)) {
            return false;
        }
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String requestUrl = request.getRequestURI();
        return accessUrl.stream().anyMatch(url -> this.checkAccessUrl(url, requestUrl));
    }

    private boolean checkAccessUrl(String url, String requestUrl) {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        return antPathMatcher.match(url, requestUrl);
    }

    /**
     * 检验时间戳是够超时
     *
     * @param invokeTime
     * @return
     */
    private boolean checkTimestamp(Date invokeTime) {
        Date now = new Date();

        long millisecondSpan = now.getTime() - invokeTime.getTime();
        //时间间隔（秒）
        int secondSpan = (int) (millisecondSpan / 1000);
        //时间间隔大于TIMESTAMP_LIMIT认为请求超时
        return secondSpan <= timestampLimit;
    }

    private void doValidate(Map<String, Object> signParams, String sign) {
        String sysKey = signParams.get("sysKey") == null ? null : String.valueOf(signParams.get("sysKey"));
        String timestamp = signParams.get("timestamp") == null ? null : String.valueOf(signParams.get("timestamp"));
        String signMethod = signParams.get("signMethod") == null ? null : String.valueOf(signParams.get("signMethod"));

        if (!StringUtils.hasText(sysKey)) {
            throw new BaseException(OpenApiCode.SYSKEY_ERROR);
        }

        //校验sysKey是否存在
        OpenApiModel config = openApiConfigService.getConfig(sysKey);
        if (config == null) {
            throw new BaseException(OpenApiCode.SYSKEY_ERROR);
        }
        //校验系统是否访问受限
        if (BooleanUtils.isFalse(config.getGlobalAccess()) && !checkAccessAuth(sysKey)) {
            throw new BaseException(OpenApiCode.ACCESSAUTH_ERROR);
        }
        //是否启用签名校验，默认开启
        if (BooleanUtils.isTrue(config.getSignCheck())) {
            String signSecret = config.getSignSecret();

            if (!StringUtils.hasText(sign) || !SignValidator.validate(signParams, signSecret, signMethod, sign)) {
                throw new BaseException(OpenApiCode.SGIN_ERROR);
            }
        }

        //是否启用时间戳校验，默认关闭
        if (BooleanUtils.isTrue(config.getTimestampCheck())) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date invokeTime = null;
            try {
                invokeTime = sdf.parse(timestamp);
            } catch (ParseException e) {
                log.error(e.getLocalizedMessage(), e);
            }
            if (invokeTime == null) {
                try {
                    //兼容毫秒时间戳
                    assert timestamp != null;
                    invokeTime = new Date(Long.parseLong(timestamp));
                } catch (Exception e) {
                    throw new BaseException(OpenApiCode.TIMESTAMP_ERROR);
                }
            }
            //已超时
            if (!checkTimestamp(invokeTime)) {
                throw new BaseException(OpenApiCode.TIMESTAMP_ERROR);
            }
        }
    }

    private void doValidate(Object body, Map<String, Object> signParams, String sign) {
        String sysKey = signParams.get("sysKey") == null ? null : String.valueOf(signParams.get("sysKey"));
        String timestamp = signParams.get("timestamp") == null ? null : String.valueOf(signParams.get("timestamp"));
        String signMethod = signParams.get("signMethod") == null ? null : String.valueOf(signParams.get("signMethod"));

        if (!StringUtils.hasText(sysKey)) {
            throw new BaseException(OpenApiCode.SYSKEY_ERROR);
        }

        //校验sysKey是否存在
        OpenApiModel config = openApiConfigService.getConfig(sysKey);
        if (config == null) {
            throw new BaseException(OpenApiCode.SYSKEY_ERROR);
        }
        //校验系统是否访问受限
        if (BooleanUtils.isFalse(config.getGlobalAccess()) && !checkAccessAuth(sysKey)) {
            throw new BaseException(OpenApiCode.ACCESSAUTH_ERROR);
        }
        //是否启用签名校验，默认开启
        if (BooleanUtils.isTrue(config.getSignCheck())) {
            String signSecret = config.getSignSecret();

            if (!StringUtils.hasText(sign) || !SignValidator.validate(body, signParams, signSecret, signMethod, sign)) {
                throw new BaseException(OpenApiCode.SGIN_ERROR);
            }
        }

        //是否启用时间戳校验，默认关闭
        if (BooleanUtils.isTrue(config.getTimestampCheck())) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date invokeTime = null;
            try {
                invokeTime = sdf.parse(timestamp);
            } catch (ParseException e) {
                log.error(e.getMessage(), e);
            }
            if (invokeTime == null) {
                try {
                    //兼容毫秒时间戳
                    assert timestamp != null;
                    invokeTime = new Date(Long.parseLong(timestamp));
                } catch (Exception e) {
                    throw new BaseException(OpenApiCode.TIMESTAMP_ERROR);
                }
            }
            //已超时
            if (!checkTimestamp(invokeTime)) {
                throw new BaseException(OpenApiCode.TIMESTAMP_ERROR);
            }
        }
    }
}
