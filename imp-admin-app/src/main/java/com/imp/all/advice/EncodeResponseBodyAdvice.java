package com.imp.all.advice;

import cn.hutool.json.JSONUtil;
import com.imp.all.framework.common.pojo.CommonResult;
import lombok.SneakyThrows;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.nio.charset.StandardCharsets;

/**
 * @author Longlin
 * @date 2022/10/14 9:20
 * @description
 * 统一对响应给客户的json进行AES加密
 */
@RestControllerAdvice
public class EncodeResponseBodyAdvice implements ResponseBodyAdvice<CommonResult<Object>> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        ImpEncode impEncode = returnType.getMethodAnnotation(ImpEncode.class);
        return impEncode != null;
    }

    @SneakyThrows
    @Override
    public CommonResult<Object> beforeBodyWrite(CommonResult<Object> body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        Object data = body.getData();
        if (data != null) {
            String s = JSONUtil.toJsonStr(data);
            body.setData(AESUtils.encrypt(s.getBytes(StandardCharsets.UTF_8)));
        }
        return body;
    }
}
