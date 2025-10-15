package com.imp.all.demos.ThreadPoolExecutor;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Longlin
 * @date 2021/3/15 14:41
 * @description
 */
public class CustomRunnable implements Runnable{

    @Setter
    @Getter
    private String bizMessage;

    public CustomRunnable(String bizMessage) {
        this.bizMessage = bizMessage;
    }

    @Override
    public void run() {
        //线程打印输出
        System.out.println("大家好，我是任务：" + bizMessage);
        try {
            //模拟线程执行时间，10s
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("任务：" + bizMessage + " 执行结束");
    }
}
