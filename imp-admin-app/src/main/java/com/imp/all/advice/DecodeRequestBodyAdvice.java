package com.imp.all.advice;

import com.imp.all.framework.web.util.HttpRequestUtil;
import lombok.SneakyThrows;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * @author Longlin
 * @date 2022/10/14 9:10
 * @description
 * 处理客户端的加密请求体、
 * 客户端使用AES算法把json数据使用AES（128位密钥）加密后，编码为Base64字符串作为请求体，请求服务器。服务器在 RequestBodyAdvice中完成解密。
 */
@RestControllerAdvice
public class DecodeRequestBodyAdvice extends RequestBodyAdviceAdapter {

    /**
     * 该方法用于判断当前请求，是否要执行beforeBodyRead方法
     * @param methodParameter handler方法的参数对象
     * @param targetType handler方法的参数类型
     * @param converterType 将会使用到的Http消息转换器类类型
     * @return 返回true则会执行beforeBodyRead
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        ImpDecode impDecode = methodParameter.getMethodAnnotation(ImpDecode.class);
        return impDecode != null;
    }

    /**
     * 在Http消息转换器执转换，之前执行
     * @param inputMessage 客户端的请求数据
     * @param parameter handler方法的参数对象
     * @param targetType handler方法的参数类型
     * @param converterType 将会使用到的Http消息转换器类类型
     * @return 返回 一个自定义的HttpInputMessage
     */
    @SneakyThrows
    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        // 读取加密的请求体(有自定义的filter所以不能这样写)
//        byte[] body = new byte[inputMessage.getBody().available()];
//        inputMessage.getBody().read(body);

        // 读取加密的请求体
        byte[] body = new byte[0];
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            body = HttpRequestUtil.getBodyContent(request).getBytes(StandardCharsets.UTF_8);
        }

        try {
            // 使用AES解密
            body = AESUtils.decrypt(Base64.getDecoder().decode(body));
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
                | BadPaddingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        // 使用解密后的数据，构造新的读取流
        InputStream rawInputStream = new ByteArrayInputStream(body);
        return new HttpInputMessage() {
            @Override
            public HttpHeaders getHeaders() {
                return inputMessage.getHeaders();
            }

            @Override
            public InputStream getBody() throws IOException {
                return rawInputStream;
            }
        };
    }

    /**
     * 在Http消息转换器执转换，之后执行
     * @param body 转换后的对象
     * @param inputMessage 客户端的请求数据
     * @param parameter handler方法的参数类型
     * @param targetType handler方法的参数类型
     * @param converterType 使用的Http消息转换器类类型
     * @return 返回一个新的对象
     */
    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
    }

    /**
     * 同上，不过这个方法处理的是，body为空的情况
     */
    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return super.handleEmptyBody(body, inputMessage, parameter, targetType, converterType);
    }
}
