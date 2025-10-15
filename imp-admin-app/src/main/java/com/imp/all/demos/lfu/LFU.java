package com.imp.all.demos.lfu;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

/**
 * @author Longlin
 * @date 2021/8/19 15:05
 * @description LFU主要体现在对元素的使用频次上。
 * LFU的缺陷是：
 * 在短期的时间内，对某些缓存的访问频次很高，这些缓存会立刻晋升为热点数据，而保证不会淘汰，这样会驻留在系统内存里面。
 * 而实际上，这部分数据只是短暂的高频率访问，之后将会长期不访问，瞬时的高频访问将会造成这部分数据的引用频率加快，而一些新加入的缓存很容易被快速删除，因为它们的引用频率很低。
 */

@Setter
@Getter
public class LFU<K, V> {

    private final int capacity;   //所有的node节点
    private final LinkedHashMap<K, Node<K, V>> caches; // 构造方法

    public LFU(int size) {
        this.capacity = size;
        this.caches = new LinkedHashMap<>(size);
    }

    public synchronized void put(K key, V value) {
        Node<K, V> node = caches.get(key); //如果新元素
        if (node == null) {
            //如果超过元素容纳量
            if (caches.size() >= capacity) {
                //移除count计数最小的那个key
                K leastKey = removeLeastCount();
                caches.remove(leastKey);
            }
            //创建新节点
            // System.currentTimeMillis() 精确到的是ms
            // System.nanoTime() 精确到的是ns级别
            node = new Node<>(key, value, System.nanoTime(), 1);
            caches.put(key, node);
        } else {
            node.setValue(value);
            node.setCount(node.getCount() + 1);
            node.setTime(System.nanoTime());
        }
        sort();
    }

    // 获取元素
    public synchronized V get(K key) {
        Node<K, V> node = caches.get(key);
        if (node != null) {
            node.setCount(node.getCount() + 1);
            node.setTime(System.nanoTime());
            sort();
            return node.getValue();
        }
        return null;
    }

    public void print() {
        for (Map.Entry<K, Node<K, V>> nodeEntry : caches.entrySet()) {
            System.out.println(nodeEntry.getValue().getValue() + ":" + nodeEntry.getValue().getCount());
        }
    }

    // 排序
    private void sort() {
        List<Map.Entry<K, Node<K, V>>> list = new ArrayList<>(caches.entrySet());
        // 次数越多，访问时间越近 越靠前
        list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        // 次数越多，访问时间越近 越靠后
//        list.sort(Map.Entry.comparingByValue());
        caches.clear();
        for (Map.Entry<K, Node<K, V>> kNodeEntry : list) {
            caches.put(kNodeEntry.getKey(), kNodeEntry.getValue());
        }
    }

    // 淘汰最小的元素这里调用了Collections.min方法，然后通过比较key的compare方法，找到计数最小和时间最长的元素，直接从缓存中移除
    private K removeLeastCount() {
        Collection<Node<K, V>> values = caches.values();
        Node<K, V> min = Collections.min(values);
        return min.getKey();
    }
}
