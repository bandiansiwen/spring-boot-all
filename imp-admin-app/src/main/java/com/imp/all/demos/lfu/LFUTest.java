package com.imp.all.demos.lfu;

/**
 * @author Longlin
 * @date 2021/8/19 15:32
 * @description
 */
public class LFUTest {

    public static void main(String[] args) {
        LFU<Integer, String> lfuCache = new LFU<>(5);
        lfuCache.put(1,  "A");
        lfuCache.put(2,  "B");
        lfuCache.put(3,  "C");
        lfuCache.put(4,  "D");
        lfuCache.put(5,  "E");
        lfuCache.put(6,  "F");
        lfuCache.get(2);
        lfuCache.get(2);
        lfuCache.get(3);
        lfuCache.get(6);
        //重新put节点3
        lfuCache.put(3, "C");

        lfuCache.print();
    }
}
