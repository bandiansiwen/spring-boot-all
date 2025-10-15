package com.imp.all.framework.web.apiVersion.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Longlin
 * @date 2021/4/22 20:46
 * @description
 */
@Setter
@Getter
@ConfigurationProperties(prefix = ApiVersionProperties.API_VERSION_PREFIX)
public class ApiVersionProperties {

    public final static String API_VERSION_PREFIX = "imp.api-version";

    private String header;
    private String enable;
}
