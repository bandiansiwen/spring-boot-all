package com.imp.all.demos.threadInterrupt.volatileFlag;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Longlin
 * @date 2022/1/9 21:34
 * @description
 */
@Slf4j
public class MyRunnable implements Runnable {

    //定义退出标志，true会一直执行，false会退出循环
    //使用volatile目的是保证可见性，一处修改了标志，处处都要去主存读取新的值，而不是使用缓存
    public volatile boolean flag = true;

    @Override
    public void run() {
        log.info("第" + Thread.currentThread().getName() + "个线程创建");

        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 退出标志生效位置
        while (flag) {
        }
        log.info("第" + Thread.currentThread().getName() + "个线程终止");
    }


}
