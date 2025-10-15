package com.imp.all.demos.designMode.createMode.singleton;

/**
 * @author Longlin
 * @date 2022/2/9 23:44
 * @description
 * 每次访问时都要同步，会影响性能，且消耗更多的资源，这是懒汉式单例的缺点。
 */
public class LazySingleton {

    //保证 instance 在所有线程中同步
    private static volatile LazySingleton instance = null;

    private LazySingleton() { //private 避免类在外部被实例化

    }

    public static synchronized LazySingleton getInstance() {
        // getInstance 方法前加同步
        if (instance == null) {
            instance = new LazySingleton();
        }
        return instance;
    }
}
