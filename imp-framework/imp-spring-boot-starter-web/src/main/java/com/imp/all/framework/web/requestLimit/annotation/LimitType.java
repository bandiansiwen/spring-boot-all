package com.imp.all.framework.web.requestLimit.annotation;

public enum LimitType {
    /**
     * 根据请求URL
     */
    URL,
    /**
     * 根据请求者IP
     */
    IP,
    /**
     * 自定义key
     */
    CUSTOMER
}