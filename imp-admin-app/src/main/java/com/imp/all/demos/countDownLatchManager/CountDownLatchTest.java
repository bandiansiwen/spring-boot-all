package com.imp.all.demos.countDownLatchManager;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author Longlin
 * @date 2022/10/18 10:26
 * @description
 * 获取群头像慢接口优化
 */
@Slf4j
public class CountDownLatchTest {

    private volatile String headerUrl = "";

    @SneakyThrows
    public String getGroupHead(String teamId) {
        // 获取缓存的群组图片
        String groupHeadUrl = getRedisGroupHeadUrl(teamId);
        if (StringUtils.isEmpty(groupHeadUrl)) {
            String latchKey = "lock:" + teamId;
            CountDownLatchModel countDownLatchModel = CountDownLatchManager.getCountDownLatchByKey(latchKey);
            CountDownLatch countDownLatch = countDownLatchModel.getCountDownLatch();
            Boolean firstThread = countDownLatchModel.getFirstThread();
            if (firstThread) {
                try {
                    groupHeadUrl = getRedisGroupHeadUrl(teamId);
                    if (StringUtils.isEmpty(groupHeadUrl)) {
                        groupHeadUrl = createGroupHeadUrl(teamId);
                    }
                } finally {
                    countDownLatch.countDown();
                    CountDownLatchManager.removeCountDownLatchByKey(latchKey);
                }
            }
            else {
                boolean await = countDownLatch.await(5, TimeUnit.SECONDS);
                groupHeadUrl = getRedisGroupHeadUrl(teamId);
            }
        }
        return groupHeadUrl;
    }

    public String getRedisGroupHeadUrl(String teamId) {
        return headerUrl;
    }

    private String createGroupHeadUrl(String teamId) {
        // 1、创建群头像
        // 2、头像地址缓存放入redis
        log.info("创建群头像");
        headerUrl = "xxx";
        return headerUrl;
    }

    public static void main(String[] args) {

        CountDownLatchTest latchTest = new CountDownLatchTest();

        for (int i = 0; i < 100; i++) {
            new Thread(new Runnable() {
                @SneakyThrows
                @Override
                public void run() {
                    String url = latchTest.getGroupHead("teamId");
                    log.info(url);
                }
            }).start();
        }
    }

}
