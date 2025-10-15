package com.imp.all.demos.Exchanger;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;

/**
 * @author Longlin
 * @date 2022/6/11 10:53
 * @description
 * Exchanger 就是线程之间的数据交换器，只能用于两个线程之间的数据交换
 */
public class ExchangerTest {

    public static void main(String[] args) throws InterruptedException {

        // 简单数据交换
        test1();
        // 超时数据交换
        test2();
        // 中断数据交换
        test3();
        // 两两数据交换
    }

    private static void test1() {
        Exchanger exchanger = new Exchanger();

        new Thread(() -> {
            try {
                Object data = "-公众号Java技术栈AAA";
                System.out.println(Thread.currentThread().getName() + data);

                // 开始交换数据
                data = exchanger.exchange(data);
                System.out.println(Thread.currentThread().getName() + data);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                Object data = "-公众号Java技术栈BBB";
                System.out.println(Thread.currentThread().getName() + data);

                // 开始交换数据
                data = exchanger.exchange(data);
                System.out.println(Thread.currentThread().getName() + data);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static void test2() {
        Exchanger exchanger = new Exchanger();

        new Thread(() -> {
            try {
                Object data = "-公众号Java技术栈AAA";
                System.out.println(Thread.currentThread().getName() + data);

                // 开始交换数据
                data = exchanger.exchange(data, 3000L, TimeUnit.MILLISECONDS);
                System.out.println(Thread.currentThread().getName() + data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static void test3() throws InterruptedException {
        Exchanger exchanger = new Exchanger();

        // 默认情况下不带超时设置会一直阻塞运行中……
        Thread thread = new Thread(() -> {
            try {
                Object data = "-公众号Java技术栈AAA";
                System.out.println(Thread.currentThread().getName() + data);

                // 开始交换数据
                data = exchanger.exchange(data);
                System.out.println(Thread.currentThread().getName() + data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        thread.start();

        // 线程中断
        Thread.sleep(3000L);
        thread.interrupt();
    }
}
