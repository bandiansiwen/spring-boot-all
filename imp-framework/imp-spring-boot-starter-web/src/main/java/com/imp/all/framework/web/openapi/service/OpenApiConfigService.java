package com.imp.all.framework.web.openapi.service;

import com.imp.all.framework.web.openapi.model.OpenApiModel;

import java.util.List;

/**
 * @author Longlin
 * @date 2023/1/16 16:13
 * @description
 */
public interface OpenApiConfigService {

    OpenApiModel getConfig(String sysKey);


    /**
     * 获取系统能访问的url地址
     * @param sysKey 系统Key
     * @return 系统能访问的URL地址
     */
    List<String> getAccessUrl(String sysKey);
}
