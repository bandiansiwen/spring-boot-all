package com.imp.all.demos.countDownLatch;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class JdbcTestManager {

    // 所有数据库的配置信息
    private HashMap<String, Object> jdbcPropertiesMap;
    private final CountDownLatch jdbcPropertiesMapLatch = new CountDownLatch(1);
    // 创建的Jdbc状态管理
    private final ConcurrentHashMap<String, CountDownLatch> sourceNameMap = new ConcurrentHashMap<>();
    // 管理数据库连接实例
    private final ConcurrentHashMap<String, JdbcObject> jdbcTemplateMap = new ConcurrentHashMap<>();

    // 设置所有的jdbc连接配置
    public void setJdbcPropertiesMap(HashMap<String, Object> jdbcPropertiesMap) {
        this.jdbcPropertiesMap = jdbcPropertiesMap;
        jdbcPropertiesMapLatch.countDown();
    }

    public JdbcObject getJdbcDatasourceByName(String sourceName) throws InterruptedException {
        // 等待连接jdbc连接配置
        jdbcPropertiesMapLatch.await();
        JdbcObject jdbcTemplate = jdbcTemplateMap.get(sourceName);
        if (jdbcTemplate == null) {
            // 避免jdbc重复创建
            if (!sourceNameMap.containsKey(sourceName)) {
                boolean isFirst = false;
                synchronized (this) {
                    if (!sourceNameMap.containsKey(sourceName)) {
                        log.info("线程：{} 已经获取jdbc配置: {}", Thread.currentThread().getId(), sourceName);
                        CountDownLatch latch = new CountDownLatch(1);
                        sourceNameMap.put(sourceName, latch);
                        // 让第一个线程创建连接池
                        isFirst = true;
                    }
                }
                if (isFirst) {
                    createJdbcTemplate(sourceName);
                }
            }
            CountDownLatch latch = sourceNameMap.get(sourceName);
            latch.await();
            jdbcTemplate = jdbcTemplateMap.get(sourceName);
        }
        return jdbcTemplate;
    }

    // 建立连接
    private void createJdbcTemplate(String sourceName) throws InterruptedException {
        Object properties = jdbcPropertiesMap.get(sourceName);
        if (properties == null) {
            throw new RuntimeException("系统不存在的数据库连接配置：" + sourceName);
        }
        // 开始创建
        Thread.sleep(1000);
        JdbcObject jdbc = new JdbcObject();
        jdbc.setName(sourceName);
        jdbcTemplateMap.put(sourceName, jdbc);
        log.info("创建成功jdbc: {}, 线程为：{}", sourceName, Thread.currentThread().getId());
        // 唤醒所有的等待的jdbc线程
        CountDownLatch latch = sourceNameMap.get(sourceName);
        latch.countDown();
    }
}