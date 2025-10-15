package com.imp.all.demos.lru;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.ListIterator;
import java.util.Map;

/**
 * @author Longlin
 * @date 2021/8/19 8:49
 * @description LRU主要体现在对元素的使用时间上
 * LRU的缺陷是：
 * 当存在热点数据时，LRU的效率很好，但偶发性的、周期性的批量操作会导致LRU命中率急剧下降，缓存污染情况比较严重。
 */
@Setter
@Getter
public class LRU<K, V> {

    private static final float hashLoadFactory = 0.75f;
    private final LinkedHashMap<K, V> map;
    private final int cacheSize;

    public LRU(int cacheSize) {
        this.cacheSize = cacheSize;
        int capacity = (int) Math.ceil(cacheSize / hashLoadFactory) + 1;
        //使用LinkedHashMap实现LRU的必要前提是将accessOrder标志位设为true以便开启按访问顺序排序的模式。
        //我们可以看到，无论是put方法还是get方法，都会导致目标Entry成为最近访问的Entry，因此就把该Entry加入到了双向链表的末尾：get方法通过调用recordAccess方法来实现；
        //put方法在覆盖已有key的情况下，也是通过调用recordAccess方法来实现，在插入新的Entry时，则是通过createEntry中的addBefore方法来实现。
        //这样，我们便把最近使用的Entry放入到了双向链表的后面。
        //多次操作后，双向链表前面的Entry便是最近没有使用的，这样当节点个数满的时候，删除最前面的Entry(head后面的那个Entry)即可，因为它就是最近最少使用的Entry。
        map = new LinkedHashMap<K, V>(capacity, hashLoadFactory, true) {
            private static final long serialVersionUID = 1;

            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > LRU.this.cacheSize;
            }
        };
    }

    public synchronized V get(K key) {
        return map.get(key);
    }

    public synchronized void put(K key, V value) {
        map.put(key, value);
    }

    public synchronized void clear() {
        map.clear();
    }

    public synchronized int usedSize() {
        return map.size();
    }

    public void print() {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            System.out.println(entry.getKey() + "--" + entry.getValue());
        }
    }

    public void reversePrint() {
        ListIterator<Map.Entry<K, V>> i = new ArrayList<>(map.entrySet()).listIterator(map.size());
        while (i.hasPrevious()) {
            Map.Entry<K, V> entry = i.previous();
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
    }
}
