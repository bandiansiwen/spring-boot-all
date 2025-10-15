package com.imp.all.framework.web.holder;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.text.NumberFormat;

/**
 * @author Longlin
 * @date 2021/10/22 17:10
 * @description
 */

@Slf4j
public class ApplicationContextHolder implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static Object getBean(String name) {
        return context.getBean(name);
    }

    public static <T> T getBean(Class<T> type) {
        if (context == null) {
            return null;
        }
        return context.getBean(type);
    }

    public static <T> T getBean(String name, Class<T> type) {
        if (context == null) {
            return null;
        }
        return context.getBean(name, type);
    }

    /**
     * 等价 {@link org.springframework.beans.factory.annotation.Value}
     */
    public static String getString(String key) {
        String strValue = context.getEnvironment().getProperty(key);
        return StrUtil.trim(strValue);
    }

    public static Number getNumber(String key) {
        try {
            return NumberFormat.getInstance().parse(getString(key));
        } catch (Exception e) {
            log.error("getNumber:{}", e.getMessage());
            return null;
        }
    }

    public static void shutDownContext() {
        int exitCode = SpringApplication.exit(context, () -> 0);
        System.exit(exitCode);
    }
}
