package com.imp.all.eventListener;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author Longlin
 * @date 2021/6/9 16:49
 * @description
 */
@Getter
public class LogSaveEvent extends ApplicationEvent {

    private LogProject logProject;

    public LogSaveEvent(Object source, LogProject logProject) {
        super(source);
        this.logProject = logProject;
    }

    public LogSaveEvent(Object source) {
        super(source);
    }
}
