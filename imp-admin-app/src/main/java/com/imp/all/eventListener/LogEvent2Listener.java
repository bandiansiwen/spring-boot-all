package com.imp.all.eventListener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author Longlin
 * @date 2021/6/9 16:52
 * @description
 */
@Component
@Slf4j
public class LogEvent2Listener implements ApplicationListener {

    @Async // 实现异步监听
    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        if (applicationEvent instanceof LogSaveEvent) {
            LogSaveEvent logSaveEvent = (LogSaveEvent) applicationEvent;
            log.info("LogEvent2Listener:"+logSaveEvent.getLogProject().getLogName());
        }
    }
}
