package com.imp.all.shardingsphere.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;

/**
 * @author Longlin
 * @date 2022/7/5 20:44
 * @description
 * 需要忽略druid连接池的默认数据源配置（@SpringBootApplication(exclude = {DruidDataSourceAutoConfigure.class})）
 */
@Configuration(proxyBeanMethods = false)
@EnableAutoConfiguration(exclude = {DruidDataSourceAutoConfigure.class})
public class ShardingSphereConfig {
}
