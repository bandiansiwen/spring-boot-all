package com.imp.all.framework.web.openapi.model;

import lombok.Data;

/**
 * @author Longlin
 * @date 2023/1/16 16:27
 * @description
 */
@Data
public class OpenApiModel {

    /**
     * 系统key
     */
    private String sysKey;
    /**
     * 系统名称
     */
    private String sysName;
    /**
     * 签名秘钥
     */
    private String signSecret;
    /**
     * 是否检查签名
     */
    private Boolean signCheck = true;

    /**
     * 全局访问
     */
    private Boolean globalAccess =true;
    /**
     * 是否检查时间戳
     */
    private Boolean timestampCheck;
}
