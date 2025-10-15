package com.imp.all.demos.lru;

/**
 * @author Longlin
 * @date 2021/8/19 15:37
 * @description
 */
public class LRUTest {

    public static void main(String[] args) {
//        LRU<Integer, String> lruCache = new LRU<>(5);
        LRUCache<Integer, String> lruCache = new LRUCache<>(5);
        lruCache.put(1,  "A");
        lruCache.put(2,  "B");
        lruCache.put(3,  "C");
        lruCache.put(4,  "D");
        lruCache.put(5,  "E");
        lruCache.put(6,  "F");
        lruCache.get(2);
        lruCache.get(2);
        lruCache.get(3);
        lruCache.get(6);
        //重新put节点3
        lruCache.put(3, "C");

        lruCache.print();

        lruCache.reversePrint();
    }
}
