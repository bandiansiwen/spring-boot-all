package com.imp.all.demos.ThreadPoolExecutor;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.concurrent.*;

/**
 * @author Longlin
 * @date 2021/3/15 14:31
 * @description
 */
@Slf4j
public class CustomAbortPolicy implements RejectedExecutionHandler {

    /**
     * 【核心方法】从FutureTask获取原始的任务对象
     * @param futureTask
     */
    private <T> T getOriginRunnable(Object futureTask) {
        boolean isFutureTask = futureTask instanceof FutureTask;
        if (!isFutureTask) {
            throw new RuntimeException("不是FutureTask 无法获取原始任务：" + "Task " + futureTask.toString());
        }

        try {
            // 获取 FutureTask.callable
            Field callableField = FutureTask.class.getDeclaredField("callable");
            callableField.setAccessible(true);
            Object callableObj = callableField.get(futureTask);

            // 获取 上一步callable的数据类型：Executors的内部类RunnableAdapter
            Class<?>[] classes = Executors.class.getDeclaredClasses();
            Class<?> tarClass = null;
            for (Class<?> cls: classes) {
                if (cls.getName().equals("java.util.concurrent.Executors$RunnableAdapter")) {
                    tarClass = cls;
                    break;
                }
            }

            // 获取原始任务对象
            assert tarClass != null;
            Field taskField = tarClass.getDeclaredField("task");
            taskField.setAccessible(true);

            return (T) taskField.get(callableObj);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("从FutureTask获取原始任务对象失败", e);
        }
    }

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {

//        CustomRunnable callable = getOriginRunnable(r);
//        System.out.println(callable.getBizMessage() + "被拒绝了，执行入库操作");
    }
}
