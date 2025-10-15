package com.imp.all.demos.delaytask.wheelTimer;

import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Longlin
 * @date 2022/1/7 16:00
 * @description
 */
@Slf4j
public class MyTimerTask implements TimerTask {

    boolean flag;
    public MyTimerTask(boolean flag) {
        this.flag = flag;
    }

    @Override
    public void run(Timeout timeout) throws Exception {
        // TODO Auto-generated method stub
        log.info("要去数据库删除订单了。。。。");
        this.flag = false;
    }
}
