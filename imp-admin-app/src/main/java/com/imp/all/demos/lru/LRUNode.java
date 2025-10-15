package com.imp.all.demos.lru;

import lombok.Data;

/**
 * @Author Daniel
 * @Date 2025/9/2/周二 22:14
 * @Description LRU 的节点
 **/

@Data
public class LRUNode<K,V> {

    private LRUNode<K,V> next;
    private LRUNode<K,V> prev;

    private K key;
    private V value;

    public LRUNode(K key, V value) {
        this.key = key;
        this.value = value;
    }
}
