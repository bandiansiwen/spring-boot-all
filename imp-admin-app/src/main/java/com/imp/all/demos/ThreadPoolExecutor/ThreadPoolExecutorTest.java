package com.imp.all.demos.ThreadPoolExecutor;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @author Longlin
 * @date 2021/3/15 13:56
 * @description
 */
public class ThreadPoolExecutorTest {

    public static void main(String[] args) {
        //核心线程数
        int corePoolSize = 3;
        //最大线程数
        int maximumPoolSize = 6;
        //超过 corePoolSize 线程数量的线程最大空闲时间
        long keepAliveTime = 2;
        //以秒为时间单位
        TimeUnit unit = TimeUnit.SECONDS;
        //创建工作队列，用于存放提交的等待执行任务
        // ArrayBlockingQueue：
        // 一个对象数组+一把锁+两个条件
        // 入队与出队都用同一把锁
        // 在只有入队高并发或出队高并发的情况下，因为操作数组，且不需要扩容，性能很高
        // 采用了数组，必须指定大小，即容量有限
        // LinkedBlockingQueue：
        // 一个单向链表+两把锁+两个条件
        // 两把锁，一把用于入队，一把用于出队，有效的避免了入队与出队时使用一把锁带来的竞争。
        // 在入队与出队都高并发的情况下，性能比ArrayBlockingQueue高很多
        // 采用了链表，最大容量为整数最大值，可看做容量无限
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(2);
        ThreadPoolExecutor threadPoolExecutor = null;
        try {
            //创建线程池
            threadPoolExecutor = new ThreadPoolExecutor(corePoolSize,
                    maximumPoolSize,
                    keepAliveTime,
                    unit,
                    workQueue,
                    new CustomAbortPolicy());
//            boolean b = threadPoolExecutor.prestartCoreThread();// 初始化一个核心线程
//            int i1 = threadPoolExecutor.prestartAllCoreThreads();   // 初始化所有核心线程，并返回初始化的线程数
            int size = threadPoolExecutor.getQueue().size();        // 队列中任务数
            int activeCount = threadPoolExecutor.getActiveCount();  // 活动线程数
            //第一种：循环提交任务 无需执行结果
//            for (int i = 0; i < 10; i++) {
//                //提交任务的索引
//                final int index = (i + 1);
//                threadPoolExecutor.submit(new CustomRunnable(String.valueOf(index)));
//                //每个任务提交后休眠500ms再提交下一个任务，用于保证提交顺序
//                Thread.sleep(500);
//            }

            //第二种：循环提交任务 需要执行结果
            long startTime = System.currentTimeMillis();
            List<Integer> integerList = Arrays.asList(0, 1, 2, 3, 4, 5);
            ThreadPoolExecutor finalThreadPoolExecutor = threadPoolExecutor;
            List<CompletableFuture<String>> collect = integerList.stream().map(i -> CompletableFuture.supplyAsync(new CustomSupplier(String.valueOf(i)), finalThreadPoolExecutor)).collect(Collectors.toList());
            // WARNING: 当任务提交入线程池超过队列长度未进入时，以下代码不执行
            List<String> result = collect.stream().map(CompletableFuture::join).collect(Collectors.toList());
            System.out.println("Elapsed time: " + (System.currentTimeMillis()-startTime));
            System.out.println("result: " + result.toString());

        } finally {
            if (threadPoolExecutor != null) {
                threadPoolExecutor.shutdown();
            }
        }
    }
}
