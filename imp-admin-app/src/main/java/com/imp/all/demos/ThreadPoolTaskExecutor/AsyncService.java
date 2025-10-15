package com.imp.all.demos.ThreadPoolTaskExecutor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author Longlin
 * @date 2021/3/29 10:51
 * @description
 */
@Service
@Slf4j
public class AsyncService {

    private final List<String> movies = Arrays.asList(
            "Forrest Gump",
            "Titanic",
            "Spirited Away",
            "The Shawshank Redemption",
            "Zootopia",
            "Farewell ",
            "Joker",
            "Crawl"
    );


    /**
     * 示范使用：找到特定字符/字符串开头的电影
     */
    @Async("taskExecutor2") // 指定线程池
    public CompletableFuture<List<String>> completableFutureTask(String start) {
        log.info(Thread.currentThread().getName() + "start this task!");
        // 找到特定字符/字符串开头的电影
        List<String> results = movies.stream().filter(movie -> movie.startsWith(start)).collect(Collectors.toList());
        // 模拟耗时任务
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 返回一个已经用完给定值完成的新的CompletableFuture
        return CompletableFuture.completedFuture(results);
    }

    /**
     * 除了 @Async 注解之外，还需要加上 @EnableAsync 注解，比如加在启动类上。
     * 默认的线程池核心线程数默认是 8，队列长度无线长。有内存溢出的风险。
     * 通过阅读 @Async 上的注解，我发现返回值只能是 void 或者 Future 类型，否则即使返回了其他值，不会报错，但是返回的值是 null，有空指针风险。
     * @Async 注解中有一个 value 属性，可以指定自定义线程池的。
     */
    @Async
    public void completableFutureTaskWithoutResult(String start) {
        //这里可能是系统对任务执行结果的处理，比如存入到数据库等等......
        log.info(Thread.currentThread().getName() + "start this task!" + start);
        // 模拟耗时任务
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
