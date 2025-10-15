package com.imp.all.scheduled;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Longlin
 * @date 2021/6/9 17:47
 * @description
 * Cron有如下两种语法格式：
 * 1.Seconds Minutes Hours DayofMonth Month DayofWeek Year
 * 2.Seconds Minutes Hours DayofMonth Month DayofWeek
 *
 * 每一个域可出现的字符如下：
 * Seconds: 可出现", - * /"四个字符，有效范围为0-59的整数
 * Minutes: 可出现", - * /"四个字符，有效范围为0-59的整数
 * Hours: 可出现", - * /"四个字符，有效范围为0-23的整数
 * DayofMonth :可出现", - * / ? L W C"八个字符，有效范围为0-31的整数
 * Month: 可出现", - * /"四个字符，有效范围为1-12的整数或JAN-DEc
 * DayofWeek: 可出现", - * / ? L C #"四个字符，有效范围为1-7的整数或SUN-SAT两个范围。1表示星期天，2表示星期一， 依次类推
 * Year: 可出现", - * /"四个字符，有效范围为1970-2099年
 *
 * 注意：由于"月份中的日期"和"星期中的日期"这两个元素互斥的,必须要对其中一个设置 "?"
 */
@Slf4j
@Component
@EnableScheduling
public class ScheduleTask {

    @Scheduled(cron = "0/30 * * * * ?")
    public void task() {
//        log.info("执行定时任务1");
    }

    @Scheduled(cron = "0/20 * * * * ?")
    public void task2() {
//        log.info("执行定时任务2");
    }
}
