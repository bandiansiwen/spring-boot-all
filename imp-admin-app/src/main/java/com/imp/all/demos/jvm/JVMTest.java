package com.imp.all.demos.jvm;

import org.openjdk.jol.info.ClassLayout;

/**
 * @author Longlin
 * @date 2021/6/23 15:22
 * @description
 */
public class JVMTest {

    public static void printLock() {
        Object o = new Object();
        System.out.println(ClassLayout.parseInstance(o).toPrintable());

        // 给对象上锁，即为修改对象的markword
        synchronized (o) {
            System.out.println(ClassLayout.parseInstance(o).toPrintable());
        }
    }

    public static void printString(String str) {
        System.out.println(ClassLayout.parseInstance(str).toPrintable());

    }

    public static void main(String[] args) {

        printLock();

        printString(new String());
        printString(new String("a"));
        printString(new String("abcdefghijklmn"));
    }
}
