package com.imp.all.framework.security.core.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Longlin
 * @date 2021/3/30 15:54
 * @description
 */
@Getter
@Setter
@ConfigurationProperties(prefix = ImpIgnoreUrlsProperties.PREFIX)
public class ImpIgnoreUrlsProperties {

    public static final String PREFIX = "imp.secure.ignored";

    private List<String> urls = new ArrayList<>();
}
