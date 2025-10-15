package com.imp.all.framework.security.core.dynamic;

import org.springframework.security.access.ConfigAttribute;

import java.util.Map;

/**
 * @author Longlin
 * @date 2021/3/30 16:03
 * @description
 */
public interface ImpDynamicSecurityService {
    /**
     * 加载资源ANT通配符和资源对应MAP
     */
    Map<String, ConfigAttribute> loadDataSource();
}
