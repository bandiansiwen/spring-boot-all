package com.imp.all.demos.countDownLatchManager;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.CountDownLatch;

/**
 * @author Longlin
 * @date 2022/10/18 10:24
 * @description
 */
public class CountDownLatchModel {
    @Setter
    @Getter
    private Boolean firstThread;
    @Setter
    @Getter
    private CountDownLatch countDownLatch;

    public CountDownLatchModel(Boolean firstThread, CountDownLatch countDownLatch) {
        this.firstThread = firstThread;
        this.countDownLatch = countDownLatch;
    }
}
