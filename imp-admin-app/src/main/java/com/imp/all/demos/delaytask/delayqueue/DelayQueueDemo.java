package com.imp.all.demos.delaytask.delayqueue;


import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author Longlin
 * @date 2022/1/7 8:56
 * @description
 */
@Slf4j
public class DelayQueueDemo {

    public static void main(String[] args) {
        // TODO Auto-generated method stub  
        List<String> list = new ArrayList<>();
        list.add("00000001");
        list.add("00000002");
        list.add("00000003");
        list.add("00000004");
        list.add("00000005");
        DelayQueue <OrderDelay> queue = new DelayQueue<>();
        long start = System.currentTimeMillis();

        for (String s : list) {
            queue.put(new OrderDelay(s, TimeUnit.NANOSECONDS.convert(3, TimeUnit.SECONDS)));
            try {
                queue.take().print();
                log.info("After " + (System.currentTimeMillis() - start) + " MilliSeconds");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
