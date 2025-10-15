package com.imp.all.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Longlin
 * @date 2024/4/9 14:49
 * @description
 */
@Slf4j
@RestController
@RequestMapping("/alert")
public class AlertManagerController {

    @PostMapping("/webhook")
    public void handleWebhook(@RequestBody String payload) {
        // 在这里处理Alert manager发出的警报
        log.info("接收到了告警：" + payload);
    }
}
