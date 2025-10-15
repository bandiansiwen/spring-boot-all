package com.imp.all.eventListener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author Longlin
 * @date 2021/6/9 16:51
 * @description
 */
@Component
@Slf4j
public class LogEventListener {

    @EventListener
    @Async // 实现异步监听
    public void saveLog(LogSaveEvent event) {
        log.info("LogEventListener:"+event.getLogProject().getLogName());
    }
}
