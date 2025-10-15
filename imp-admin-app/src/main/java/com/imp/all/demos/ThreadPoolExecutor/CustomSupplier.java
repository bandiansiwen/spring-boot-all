package com.imp.all.demos.ThreadPoolExecutor;

import lombok.Getter;
import lombok.Setter;

import java.util.function.Supplier;

/**
 * @author Longlin
 * @date 2021/3/29 20:31
 * @description
 */
public class CustomSupplier implements Supplier<String> {

    @Setter
    @Getter
    private String bizMessage;

    public CustomSupplier(String bizMessage) {
        this.bizMessage = bizMessage;
    }

    @Override
    public String get() {
        //线程打印输出
        System.out.println("大家好，我是任务：" + bizMessage);
        try {
            //模拟线程执行时间，10s
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("任务：" + bizMessage + " 执行结束");
        return bizMessage;
    }
}
