package com.imp.all.demos.lru;

import lombok.Data;

import java.util.HashMap;

/**
 * @Author Daniel
 * @Date 2025/9/2/周二 22:13
 * @Description LRU 简单实现
 **/
@Data
public class LRUCache<K,V> {

    // 设定最大容量
    private final int capacity;
    // 当前容量
    private int size;
    // 链表头尾节点
    LRUNode<K,V> head = new LRUNode<>(null, null);
    LRUNode<K,V> tail = new LRUNode<>(null, null);
     {
         head.setNext(tail);
     }
    // 用作判断节点是否存在
    private HashMap<K, LRUNode<K,V>> cache = new HashMap<>();

    public LRUCache(int capacity) {
        this.capacity = capacity;
    }

    public LRUNode<K,V> get(K key) {
        LRUNode<K,V> node = cache.get(key);
        if (null == node) {
            return null;
        }
        moveToHead(node);
        return node;
    }

    public void put(K key, V value) {
        LRUNode<K,V> node = get(key);
        if (null == node) {
            node = new LRUNode<>(key, value);
            cache.put(key, node);
            addToHead(node);
            size++;
            if (size > capacity) {
                LRUNode<K, V> removedNode = removeTail();
                cache.remove(removedNode.getKey());
                size--;
                System.out.println("Removed " + removedNode.getKey() + " from cache");
            }
        }
        else {
            node.setValue(value);
            moveToHead(node);
        }
    }

    private void addToHead(LRUNode<K,V> node) {
        node.setPrev(head);
        node.setNext(head.getNext());
        head.getNext().setPrev(node);
        head.setNext(node);
    }

    private void removeNode(LRUNode<K,V> node) {
        node.getPrev().setNext(node.getNext());
        node.getNext().setPrev(node.getPrev());
        node.setNext(null);
        node.setPrev(null);
    }

    private void moveToHead(LRUNode<K,V> node) {
        removeNode(node);
        addToHead(node);
    }

    private LRUNode<K,V> removeTail() {
        LRUNode<K,V> node = tail.getPrev();
        removeNode(node);
        return node;
    }

    public void print() {
        LRUNode<K, V> node = head.getNext();
        while (node != tail && node != null) {
            System.out.println(node.getKey() + ":" + node.getValue());
            node = node.getNext();
        }
    }

    public void reversePrint() {
        LRUNode<K, V> node = tail.getPrev();
        while (node != head && node != null) {
            System.out.println(node.getKey() + "--" + node.getValue());
            node = node.getPrev();
        }
    }
}
