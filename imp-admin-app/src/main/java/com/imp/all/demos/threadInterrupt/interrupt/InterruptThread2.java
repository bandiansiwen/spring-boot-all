package com.imp.all.demos.threadInterrupt.interrupt;

/**
 * @author Longlin
 * @date 2022/1/9 21:51
 * @description
 */
public class InterruptThread2 extends Thread {

    public static void main(String[] args) {
        try {
            InterruptThread2 t = new InterruptThread2();
            t.start();
            Thread.sleep(1);
            // interrupt 方法停止运行的的线程
            t.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        for(int i = 0; i <= 100; i++) {
            // 运行线程里面判断中断标志位
            if (Thread.currentThread().isInterrupted()) {
                break;
            }
            System.out.println("i=" + i);
        }
    }
}
