package com.imp.all.demos.countDownLatch;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Longlin
 * @date 2021/7/5 12:17
 * @description
 */
@Setter
@Getter
@Slf4j
public class JdbcObject {

    private String name;

    public void test() {
//        log.info("线程：{}  执行JdbcObject: {}", Thread.currentThread().getId(), name);
    }
}
