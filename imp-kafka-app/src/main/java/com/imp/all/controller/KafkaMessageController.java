package com.imp.all.controller;

import com.imp.all.framework.common.pojo.CommonResult;
import com.imp.all.producer.KafkaProducer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Longlin
 * @date 2021/6/3 11:01
 * @description
 */
@RestController
@RequestMapping("/kafka/message")
public class KafkaMessageController {

    @Resource
    private KafkaProducer kafkaProducer;

    @PostMapping("/send")
    public CommonResult<?> sendMessage(@RequestParam String topic,
                                       @RequestParam String message) {
        return kafkaProducer.send(topic, message);
    }

    @PostMapping("/syncSend") // 同步发送
    public CommonResult<?> syncSendMessage(@RequestParam String topic,
                                           @RequestParam String message){
        return kafkaProducer.syncSend(topic, message);
    }
}
