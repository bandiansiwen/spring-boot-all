package com.imp.all.demos.countDownLatchManager;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * @author Longlin
 * @date 2022/10/18 10:25
 * @description
 */
public class CountDownLatchManager {
    private static final ConcurrentHashMap<String, CountDownLatch> lockManager = new ConcurrentHashMap<>();

    public static CountDownLatchModel getCountDownLatchByKey(String key) {
        CountDownLatch countDownLatch = lockManager.get(key);
        boolean firstThread = false;
        if (countDownLatch == null) {
            synchronized (CountDownLatchManager.class) {
                countDownLatch = lockManager.get(key);
                if (countDownLatch == null) {
                    countDownLatch = new CountDownLatch(1);
                    lockManager.put(key, countDownLatch);
                    firstThread = true;
                }
            }
        }
        return new CountDownLatchModel(firstThread, countDownLatch);
    }

    public static void removeCountDownLatchByKey(String key) {
        lockManager.remove(key);
    }
}
