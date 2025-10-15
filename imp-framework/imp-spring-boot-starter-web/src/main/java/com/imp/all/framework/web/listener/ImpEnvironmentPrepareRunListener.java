package com.imp.all.framework.web.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.io.IOException;

/**
 * @author Longlin
 * @date 2023/1/12 10:03
 * @description 环境初始化监听器，用于自动加载commons.properties
 */

@Slf4j
public class ImpEnvironmentPrepareRunListener implements SpringApplicationRunListener, Ordered {

    private final SpringApplication application;
    private final String[] args;

    private static ConfigurableBootstrapContext bootstrapContext;
    private static ConfigurableEnvironment configurableEnvironment;

    private final static String[] COMMONS_PROPS_LOCATIONS = new String[]{
            "file:./commons.properties",
            "file:/apps/conf/commons.properties",
            "classpath:/commons.properties"
    };

    public ImpEnvironmentPrepareRunListener(SpringApplication application, String[] args) {
        this.application = application;
        this.args = args;
    }

    @Override
    public void environmentPrepared(ConfigurableBootstrapContext bootstrapContext, ConfigurableEnvironment environment) {
        ImpEnvironmentPrepareRunListener.bootstrapContext = bootstrapContext;
        ImpEnvironmentPrepareRunListener.configurableEnvironment = environment;

        boolean loadCommonsProps = false;
        StringBuilder errors = new StringBuilder();
        for (String location : COMMONS_PROPS_LOCATIONS) {
            Resource resource = new DefaultResourceLoader().getResource(location);
            PropertySourceFactory factory = new DefaultPropertySourceFactory();
            try {
                addPropertySource(environment, factory.createPropertySource(location, new EncodedResource(resource, "UTF-8")));
                System.out.println("***loaded commons.properties from " + location);
                loadCommonsProps = true;
                break;
            } catch (IOException e) {
                errors.append(e.getMessage()).append("\n");
                // e.printStackTrace();
            }
        }

        if (!loadCommonsProps) {
            log.error("no commons.properties found!");
            log.debug("no commons.properties found! details:{}", errors);
        }
    }

    private void addPropertySource(ConfigurableEnvironment environment, org.springframework.core.env.PropertySource<?> propertySource) {
        MutablePropertySources propertySources = environment.getPropertySources();
        propertySources.addLast(propertySource);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
