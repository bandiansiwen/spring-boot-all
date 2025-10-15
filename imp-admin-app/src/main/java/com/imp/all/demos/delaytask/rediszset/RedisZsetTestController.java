package com.imp.all.demos.delaytask.rediszset;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

/**
 * @author Longlin
 * @date 2022/1/9 22:12
 * @description
 * 这种方案会出现多个线程消费同一个资源的情况
 * 解决方案：
 * (1)用分布式锁，但是用分布式锁，性能下降了，不采用。
 * (2)对remove的返回值进行判断，只有大于0的时候，才消费数据，
 */
@Slf4j
@RestController
@RequestMapping("/rediszset")
public class RedisZsetTestController {

    private static final String REDIS_ZSET_KEY = "OrderId";

    private volatile boolean flag = true;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/product")
    public String productDelayMessage() {
        for (int i = 0; i < 5; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.SECOND, 3*(i+1));
            int second3later = (int) (calendar.getTimeInMillis()/1000);
            ZSetOperations<String, Object> zSet = redisTemplate.opsForZSet();
            zSet.add(REDIS_ZSET_KEY, "OID0000001" + i, second3later);
        }
        log.info(DateUtil.toLocalDateTime(new Date()) + ", 投递任务成功");
        return "success";
    }

    @GetMapping("/consumer")
    public String consumerDelayMessage() {
        flag = true;
        new Thread(() -> {
            while (true) {
                if (!flag) {
                    log.info("消费延时任务线程关闭");
                    break;
                }
                ZSetOperations<String, Object> zSet = redisTemplate.opsForZSet();
                Set<ZSetOperations.TypedTuple<Object>> items = zSet.rangeWithScores(REDIS_ZSET_KEY, 0, 1);
                if (items == null || items.isEmpty()) {
                    log.info("当前没有等待的任务");
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
                for (ZSetOperations.TypedTuple<Object> item : items) {
                    int score = Objects.requireNonNull(item.getScore()).intValue();
                    Calendar cal = Calendar.getInstance();
                    int nowSecond = (int) (cal.getTimeInMillis() / 1000);
                    if (nowSecond >= score) {
                        String orderId = (String) item.getValue();
                        Long count = zSet.remove(REDIS_ZSET_KEY, orderId);
                        if (count!=null && count>0) {
                            log.info(DateUtil.toLocalDateTime(new Date()) + ", redis消费了一个任务：消费的订单OrderId为" + orderId);
                        }
                    }
                }
            }
        }).start();

        return "start to consumer";
    }

    @GetMapping("/stopConsumer")
    public String stopConsumerDelayMessage() {
        flag = false;
        return "consumer is stopped";
    }
}
