package com.imp.all.framework.file.config;

import com.imp.all.framework.file.core.client.ImpFileClientFactory;
import com.imp.all.framework.file.core.client.ImpFileClientFactoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Longlin
 * @date 2022/11/21 16:04
 * @description
 */
@Configuration(proxyBeanMethods = false) //告诉SpringBoot这是一个配置类 == 配置文件
public class ImpFileAutoConfiguration {

    @Bean
    public ImpFileClientFactory fileClientFactory() {
        return new ImpFileClientFactoryImpl();
    }

}
