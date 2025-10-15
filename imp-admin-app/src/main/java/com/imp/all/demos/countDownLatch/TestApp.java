package com.imp.all.demos.countDownLatch;

import lombok.SneakyThrows;

import java.util.HashMap;

/**
 * @author Longlin
 * @date 2021/7/5 12:10
 * @description
 */

public class TestApp {

    public static void main(String[] args) {

        JdbcTestManager jdbcTestManager = new JdbcTestManager();

        for (int i = 0; i < 100; i++) {
            if (i % 3 == 0) {
                new Thread(new Runnable() {
                    @SneakyThrows
                    @Override
                    public void run() {
                        JdbcObject jdbc1 = jdbcTestManager.getJdbcDatasourceByName("jdbc0");
                        jdbc1.test();
                    }
                }).start();
            }
            if (i % 3 == 1) {
                new Thread(new Runnable() {
                    @SneakyThrows
                    @Override
                    public void run() {
                        JdbcObject jdbc1 = jdbcTestManager.getJdbcDatasourceByName("jdbc1");
                        jdbc1.test();
                    }
                }).start();
            }
            if (i % 3 == 2) {
                new Thread(new Runnable() {
                    @SneakyThrows
                    @Override
                    public void run() {
                        JdbcObject jdbc1 = jdbcTestManager.getJdbcDatasourceByName("jdbc2");
                        jdbc1.test();
                    }
                }).start();
            }

            // 给manager所有jdbc的配置
            if (i == 88) {
                HashMap map = new HashMap<>();
                map.put("jdbc0", new Object());
                map.put("jdbc1", new Object());
                map.put("jdbc2", new Object());
                jdbcTestManager.setJdbcPropertiesMap(map);
            }
        }

        while (true);
    }
}
