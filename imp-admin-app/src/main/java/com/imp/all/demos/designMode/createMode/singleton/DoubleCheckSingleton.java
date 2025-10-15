package com.imp.all.demos.designMode.createMode.singleton;

/**
 * @author Longlin
 * @date 2022/2/9 23:54
 * @description
 */
public class DoubleCheckSingleton {

    private static volatile DoubleCheckSingleton instance = null;

    public static DoubleCheckSingleton getInstance() {
        if (instance == null) {
            synchronized(DoubleCheckSingleton.class) {
                if (instance == null) {
                    instance = new DoubleCheckSingleton();
                }
            }
        }
        return instance;
    }

}
