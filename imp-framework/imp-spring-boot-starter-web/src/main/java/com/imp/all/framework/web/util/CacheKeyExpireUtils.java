package com.imp.all.framework.web.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Longlin
 * @date 2023/3/31 17:29
 * @description
 */
@Slf4j
public class CacheKeyExpireUtils {
    private static final AtomicReference<Properties> PROPERTIES_ATOMIC_REFERENCE = new AtomicReference<>();

    public static long getExpireTime(String keyPrefix, long defaultVal) {
        if (PROPERTIES_ATOMIC_REFERENCE.get() == null) {
            try (InputStream is = CacheKeyExpireUtils.class.getClassLoader().getResourceAsStream("cacheKeyExpire.properties")) {
                if (is == null) {
                    log.warn("cacheKeyExpire.properties read fail");
                    return defaultVal;
                }
                Properties properties = new Properties();
                properties.load(is);
                PROPERTIES_ATOMIC_REFERENCE.compareAndSet(null, properties);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        Properties properties = PROPERTIES_ATOMIC_REFERENCE.get();
        if (properties == null || properties.isEmpty()) {
            return defaultVal;
        }
        String property = properties.getProperty(keyPrefix);
        if (StringUtils.isNotBlank(property)) {
            return Long.parseLong(property);
        }
        return defaultVal;
    }
}
