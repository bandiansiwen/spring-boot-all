package com.imp.all.demos.threadInterrupt.volatileFlag;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Longlin
 * @date 2022/1/9 21:38
 * @description
 */
@Slf4j
public class TreadTest {

    public static void main(String[] args) throws InterruptedException {
        MyRunnable runnable = new MyRunnable();

        // 创建4个线程
        for (int i=0;i<5;i++) {
            Thread thread = new Thread(runnable, i+"");
            thread.start();
        }

        // 线程休眠
        Thread.sleep(2000L);
        log.info("————————————————————");

        //修改退出标志，使线程终止
        runnable.flag = false;
    }
}
