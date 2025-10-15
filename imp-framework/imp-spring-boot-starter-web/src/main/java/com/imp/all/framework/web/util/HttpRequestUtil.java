package com.imp.all.framework.web.util;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author Longlin
 * @date 2021/4/13 15:18
 * @description
 */
public class HttpRequestUtil {

    // 获取请求的真实IP地址
    public static String getRequestIp(HttpServletRequest request) {
        //通过HTTP代理服务器转发时添加
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            // 从本地访问时根据网卡取本机配置的IP
            if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
                InetAddress inetAddress = null;
                try {
                    inetAddress = InetAddress.getLocalHost();
                    ipAddress = inetAddress.getHostAddress();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }
        // 通过多个代理转发的情况，第一个IP为客户端真实IP，多个IP会按照','分割
        if (ipAddress != null && ipAddress.length() > 15) {
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }

    // 获取Parameter内容
    public static String getParameterContent(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        Map<String, String[]> map = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : map.entrySet()) {
            String key = entry.getKey();
            String[] value = entry.getValue();
            sb.append(key).append(":").append(Arrays.toString(value));
        }
        return sb.toString();
    }

    // 获取Parameter内容
    public static Map<String, Object> getParameterMapContent(HttpServletRequest request) {
        Map<String, Object> paramMap = new HashMap<>();
        Enumeration<String> enumeration = request.getParameterNames();
        while (enumeration.hasMoreElements()) {
            String key = enumeration.nextElement();
            paramMap.put(key, request.getParameter(key));
        }
        return paramMap;
    }

    // 获取请求体内容
    public static String getBodyContent(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        try {
            ServletInputStream inputStream = request.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String getRequestHeader(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {         //读取请求消息头
            String name = headerNames.nextElement();
            sb.append(name).append(":").append(request.getHeader(name)).append(";");
        }
        return sb.toString();
    }

    public static String getRequestHeader(HttpServletResponse response) {
        StringBuilder sb = new StringBuilder();
        Collection<String> headerNames = response.getHeaderNames();
        for (String name : headerNames) {         //读取请求消息头
            sb.append(name).append(":").append(response.getHeader(name)).append(";");
        }
        return sb.toString();
    }
}
