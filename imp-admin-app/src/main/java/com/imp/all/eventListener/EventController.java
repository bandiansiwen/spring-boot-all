package com.imp.all.eventListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/event")
public class EventController {

    @Autowired
    private ApplicationContext publisher;

    @GetMapping("/publisher")
    public String publisher(){
        LogSaveEvent event = new LogSaveEvent(this,new LogProject("tom"));
        publisher.publishEvent(event);
        return "success";
    }
}