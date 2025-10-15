package com.imp.all.lifeCycle;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

/**
 * 在Springboot中，其实已经帮你实现好了一个shutdownHook，支持响应Ctrl+c或者kill -15 TERM信号。
 */
@Component
@Slf4j
public class GracefulShutdownListener implements ApplicationListener<ContextClosedEvent> {
    
    @Override
    public void onApplicationEvent(@NonNull ContextClosedEvent contextClosedEvent) {
        //注销逻辑 优雅下线
        log.info("服务下线");
    }
}