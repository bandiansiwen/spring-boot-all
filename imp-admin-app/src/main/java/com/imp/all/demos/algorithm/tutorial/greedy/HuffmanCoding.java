package com.imp.all.demos.algorithm.tutorial.greedy;

import java.util.*;

/**
 * @Author Daniel
 * @Date 2025/11/16/周日 18:47
 * @Description 霍夫曼编码
 *
 * 霍夫曼编码
 * 用于数据压缩的霍夫曼编码，是一种变长编码，通过贪心算法构建最优前缀码。
 **/
public class HuffmanCoding {
    static class Node implements Comparable<Node> {
        char ch;
        int freq;
        Node left, right;
        
        Node(char ch, int freq) {
            this.ch = ch;
            this.freq = freq;
        }
        
        Node(int freq, Node left, Node right) {
            this.freq = freq;
            this.left = left;
            this.right = right;
        }
        
        @Override
        public int compareTo(Node other) {
            return this.freq - other.freq;
        }
    }
    
    public Map<Character, String> buildHuffmanCodes(String text) {
        // 统计字符频率
        Map<Character, Integer> freqMap = new HashMap<>();
        for (char ch : text.toCharArray()) {
            freqMap.put(ch, freqMap.getOrDefault(ch, 0) + 1);
        }
        
        // 创建优先队列（最小堆）
        PriorityQueue<Node> pq = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> entry : freqMap.entrySet()) {
            pq.offer(new Node(entry.getKey(), entry.getValue()));
        }
        
        // 构建霍夫曼树
        while (pq.size() > 1) {
            Node left = pq.poll();
            Node right = pq.poll();
            Node parent = new Node(left.freq + right.freq, left, right);
            pq.offer(parent);
        }
        
        // 生成编码
        Map<Character, String> codes = new HashMap<>();
        buildCodes(pq.peek(), "", codes);
        return codes;
    }
    
    private void buildCodes(Node node, String code, Map<Character, String> codes) {
        if (node == null) return;
        
        if (node.left == null && node.right == null) {
            codes.put(node.ch, code);
            return;
        }
        
        buildCodes(node.left, code + "0", codes);
        buildCodes(node.right, code + "1", codes);
    }
    
    public static void main(String[] args) {
        HuffmanCoding huffman = new HuffmanCoding();
        String text = "abracadabra";
        Map<Character, String> codes = huffman.buildHuffmanCodes(text);
        
        System.out.println("霍夫曼编码:");
        for (Map.Entry<Character, String> entry : codes.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}