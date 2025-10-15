package com.imp.all.demos.lfu;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Longlin
 * @date 2021/8/19 15:01
 * @description
 */
@Setter
@Getter
public class Node<K,V> implements Comparable<Node<K,V>> {

    private K key;
    private V value;
    private long time; // 访问时间
    private int count; // 访问次数

    public Node(K key, V value, long time, int count) {
        this.key = key;
        this.value = value;
        this.time = time;
        this.count = count;
    }

    // 首先比较节点的访问次数，在访问次数相同的情况下比较节点的访问时间
    @Override
    public int compareTo(Node o) {
        int compare = Integer.compare(this.count, o.count); //在数目相同的情况下 比较时间
        if (compare == 0) {
            return Long.compare(this.time, o.time);
        }
        return compare;
    }
}
