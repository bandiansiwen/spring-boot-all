package com.imp.all.framework.web.util;

import cn.hutool.json.JSONUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Longlin
 * @date 2021/4/14 21:58
 * @description
 */
public class HttpResultUtil {

    public static void responseJson(HttpServletResponse response, Object result) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control","no-cache");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().println(JSONUtil.parse(result));
        response.getWriter().flush();
    }

    public static void responseJson(HttpServletResponse response, int statusCode, Object result) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control","no-cache");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setStatus(statusCode);
        response.getWriter().println(JSONUtil.parse(result));
        response.getWriter().flush();
    }
}
