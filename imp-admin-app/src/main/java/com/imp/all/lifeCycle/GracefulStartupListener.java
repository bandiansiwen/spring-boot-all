package com.imp.all.lifeCycle;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GracefulStartupListener implements ApplicationListener<ApplicationReadyEvent> {
    @Override
    public void onApplicationEvent(@NonNull ApplicationReadyEvent applicationReadyEvent){
       //注册逻辑 优雅上线
        log.info("服务上线");
    }
}