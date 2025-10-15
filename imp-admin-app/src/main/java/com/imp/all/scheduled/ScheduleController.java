package com.imp.all.scheduled;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * @author Longlin
 * @date 2021/6/9 17:59
 * @description
 */
@RestController
@Slf4j
@RequestMapping("/scheduled")
public class ScheduleController {

    private final static ConcurrentHashMap<String, Task> taskMap = new ConcurrentHashMap<>();

    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(10);
        return threadPoolTaskScheduler;
    }

    @Slf4j
    @Data
    static class Task {
        private String taskId;
        private Runnable runnable;
        private ScheduledFuture<?> scheduledFuture;
        private String cron;

        public Task(String taskId) {
            this.taskId = taskId;
            runnable = () -> {
                log.info("任务taskId:" + taskId);
            };
            cron = "0/5 * * * * ?";
        }
    }

    /**
     * 创建定时任务
     */
    @GetMapping("/startTask/{id}")
    public String start(@PathVariable("id") String id) {
        if (StringUtils.isNotEmpty(id) && !taskMap.containsKey(id)) {
            Task task = new Task(id);
            ScheduledFuture<?> future = threadPoolTaskScheduler.schedule(task.getRunnable(),
                    triggerContext -> new CronTrigger(task.getCron())
                            .nextExecutionTime(triggerContext)
            );
            task.setScheduledFuture(future);
            taskMap.put(task.getTaskId(), task);
        }
        return "success";
    }

    /**
     * 更新定时任务的cron
     */
    @PostMapping("/updateCron/{id}")
    public String update(@RequestBody String cron, @PathVariable("id") String id) {
        if (taskMap.containsKey(id)) {
            Task task = taskMap.get(id);
            ScheduledFuture<?> future = null;
            try {
                future = threadPoolTaskScheduler.schedule(task.getRunnable(),
                        triggerContext -> new CronTrigger(cron).nextExecutionTime(triggerContext)
                );
            } catch (Exception e) {
                return "error";
            }
            task.getScheduledFuture().cancel(true);
            task.setScheduledFuture(future);
        }
        return "success";
    }

    /**
     * 删除定时任务
     */
    @DeleteMapping("/stopTask/{id}")
    public String stop(@PathVariable("id") String id) {
        if (taskMap.containsKey(id)) {
            Task task = taskMap.get(id);
            task.getScheduledFuture().cancel(true);
            taskMap.remove(id);
        }
        return "success";
    }
}
