package com.imp.all.demos.designMode.createMode.singleton;

/**
 * @author Longlin
 * @date 2022/2/10 0:02
 * @description 基于类初始化
 */
public class Singleton {
    static class SingletonHolder {
        static Singleton instance = new Singleton();
    }

    public static Singleton getInstance(){
        return SingletonHolder.instance; //这里将导致内部类InstanceHolder初始化
    }
}
