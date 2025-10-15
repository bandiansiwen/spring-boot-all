package com.imp.all.framework.security.core.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Longlin
 * @date 2021/3/30 11:34
 * @description
 */
@Getter
@Setter
@ConfigurationProperties(prefix = ImpJwtProperties.PREFIX)
public class ImpJwtProperties {

    public static final String PREFIX = "imp.jwt";

    private String tokenHeader;
    private String secret;
    private Long expiration;
    private String tokenPrefix;
}
