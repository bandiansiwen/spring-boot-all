package com.imp.all.demos.netty;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Longlin
 * @date 2021/4/27 14:27
 * @description
 */
@Configuration
@ConditionalOnProperty(value = "webSocket.netty.enable", havingValue = "true")
public class NettyConfig {

    @Bean
    public NettyServer nettyServer() {
        return new NettyServer();
    }
}
