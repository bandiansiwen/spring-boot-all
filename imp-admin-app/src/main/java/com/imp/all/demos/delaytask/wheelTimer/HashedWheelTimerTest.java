package com.imp.all.demos.delaytask.wheelTimer;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timer;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author Longlin
 * @date 2022/1/7 15:59
 * @description
 */
@Slf4j
public class HashedWheelTimerTest {

    public static void main(String[] args) {
        MyTimerTask timerTask = new MyTimerTask(true);
        Timer timer = new HashedWheelTimer();
        timer.newTimeout(timerTask, 5, TimeUnit.SECONDS);
        int i = 1;
        while(timerTask.flag) {
            try {
                Thread.sleep(1000);
            }
            catch(InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            log.info(i + "秒过去了");
            i++;
        }
        log.info("done");
    }
}
