package com.imp.all.demos.algorithm.compare;

import java.util.HashSet;
import java.util.concurrent.*;

/**
 * 如何在20秒内完成两个库各100万条数据的快速比对？
 */

public class DataCompare {

    private static final int NUM_OF_THREADS = 8;
    private static final long DATA_SIZE = 1000000; //100万条数据

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // 创建两个包含100万条数据的HashSet
        HashSet<String> set1 = createData(DATA_SIZE);
        String[] set1Array = set1.toArray(new String[0]);
        HashSet<String> set2 = createData(DATA_SIZE);

        // 创建固定大小的线程池
        ExecutorService executor = Executors.newFixedThreadPool(NUM_OF_THREADS);
        CompletionService<Boolean> completionService = new ExecutorCompletionService<>(executor);

        // 记录开始时间
        long startTime = System.currentTimeMillis();

        // 提交任务到线程池
        for (int i = 0; i < DATA_SIZE; i += (DATA_SIZE / NUM_OF_THREADS)) {
            final int start = i;
            completionService.submit(() -> {
                for (int j = start; j < start + (DATA_SIZE / NUM_OF_THREADS); j++) {
                    if (!set2.contains(set1Array[j])) {
                        return false;
                    }
                }
                return true;
            });
        }

        // 等待所有任务完成
        int finishedTasks = 0;
        boolean isIdentical = true;
        while (finishedTasks < NUM_OF_THREADS) {
            Future<Boolean> future = completionService.take();
            if (!future.get()) {
                isIdentical = false;
                break;
            }
            finishedTasks++;
        }

        // 关闭线程池
        executor.shutdown();

        // 记录结束时间
        long endTime = System.currentTimeMillis();
        System.out.println("是否相同: " + isIdentical);
        System.out.println("耗时: " + (endTime - startTime) + "毫秒");
    }

    // 创建数据集
    private static HashSet<String> createData(long size) {
        HashSet<String> setData = new HashSet<>();
        for (int i = 0; i < size; i++) {
            setData.add("item" + i);
        }
        return setData;
    }

}
