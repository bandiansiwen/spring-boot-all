package com.imp.all.controller;

import com.imp.all.framework.common.pojo.CommonResult;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Longlin
 * @date 2021/6/11 10:40
 * @description
 */
@RestController
public class KafkaConsumerController {

    @Resource
    private KafkaListenerEndpointRegistry registry;

    @GetMapping("/stop")
    public CommonResult<?> stop() {
        registry.getListenerContainer("aaaListener").pause();
        return CommonResult.success();
    }

    @GetMapping("/start")
    public CommonResult<?> start() {
        MessageListenerContainer listenerContainer = registry.getListenerContainer("aaaListener");
        if (!listenerContainer.isRunning()) {
            listenerContainer.start();
        }
        listenerContainer.resume();
        return CommonResult.success();
    }
}
