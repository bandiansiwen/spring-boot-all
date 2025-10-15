package com.imp.all.framework.web.apiVersion.config;

import org.springframework.web.servlet.mvc.condition.RequestCondition;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Longlin
 * @date 2021/4/22 18:08
 * @description
 */
public class ApiVersionCondition implements RequestCondition<ApiVersionCondition> {

    /**
     * header 指定版本号请求头
     */
    public static final String HEADER_VERSION = "x-api-version";

    //api版本号
    private final String apiVersion;

    public ApiVersionCondition(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getApiVersion() {
        return apiVersion;
    }
    /**
     * 将不同的筛选条件进行合并
     */
    @Override
    public ApiVersionCondition combine(ApiVersionCondition apiVersionCondition) {
        // 采用最后定义优先原则，则方法上的定义覆盖类上面的定义
        return new ApiVersionCondition(apiVersionCondition.getApiVersion());
    }

    /**
     * 根据request的header版本号进行查找匹配的筛选条件
     */
    @Override
    public ApiVersionCondition getMatchingCondition(HttpServletRequest httpServletRequest) {
        String version = httpServletRequest.getHeader(HEADER_VERSION);
        if(version != null){
            if (compareTo(version,this.apiVersion)) {
                return this;
            }
        }
        return null;
    }

    /**
     * 版本比对，用于排序
     */
    @Override
    public int compareTo(ApiVersionCondition apiVersionCondition, HttpServletRequest httpServletRequest) {
        //优先匹配最新版本号
        return compareTo(apiVersionCondition.getApiVersion(),this.apiVersion) ? 1 : -1;
    }

    private boolean compareTo(String version1,String version2){
        String[] split1 = version1.split("\\.");
        String[] split2 = version2.split("\\.");
        if (split1.length != split2.length) {
            return false;
        }
        for (int i = 0; i < split1.length; i++) {
            if (Integer.parseInt(split1[i])<Integer.parseInt(split2[i])){
                return false;
            }
        }
        return true;
    }
}
