package com.imp.all.demos.threadInterrupt.interrupt;

/**
 * @author Longlin
 * @date 2022/1/9 21:51
 * @description
 */
public class InterruptThread1 extends Thread {

    public static void main(String[] args) {
        try {
            InterruptThread1 t = new InterruptThread1();
            t.start();
            Thread.sleep(20);
            // interrupt 方法停止阻塞的线程
            t.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            // 阻塞线程捕获异常并结束自己
            e.printStackTrace();
            return;
        }
        for(int i = 0; i <= 10; i++) {
            System.out.println("i=" + i);
        }
    }
}
