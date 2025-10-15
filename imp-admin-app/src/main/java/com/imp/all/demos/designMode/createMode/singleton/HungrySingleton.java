package com.imp.all.demos.designMode.createMode.singleton;

/**
 * @author Longlin
 * @date 2022/2/9 23:48
 * @description
 * 证在调用 getInstance 方法之前单例已经存在
 */
public class HungrySingleton {

    private static final HungrySingleton instance = new HungrySingleton();

    private HungrySingleton() {

    }

    public static HungrySingleton getInstance() {
        return instance;
    }
}
