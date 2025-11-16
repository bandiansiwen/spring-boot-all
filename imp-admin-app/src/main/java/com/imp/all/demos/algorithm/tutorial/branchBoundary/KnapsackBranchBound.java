package com.imp.all.demos.algorithm.tutorial.branchBoundary;

import java.util.*;

/**
 * @Author Daniel
 * @Date 2025/11/16/周日 18:47
 * @Description 0-1背包问题（分支限界解法）
 *
 * 同动态规划中的0-1背包问题，但是使用分支限界法求解。
 **/
public class KnapsackBranchBound {
    static class Item implements Comparable<Item> {
        int weight;
        int value;
        double ratio; // 价值密度
        
        Item(int weight, int value) {
            this.weight = weight;
            this.value = value;
            this.ratio = (double) value / weight;
        }
        
        @Override
        public int compareTo(Item other) {
            return Double.compare(other.ratio, this.ratio); // 按价值密度降序
        }
    }
    
    static class Node implements Comparable<Node> {
        int level;          // 当前处理的物品层级
        int profit;         // 当前已获得的价值
        int weight;         // 当前已使用的重量
        double bound;       // 价值上界
        
        Node(int level, int profit, int weight) {
            this.level = level;
            this.profit = profit;
            this.weight = weight;
            this.bound = 0;
        }
        
        @Override
        public int compareTo(Node other) {
            return Double.compare(other.bound, this.bound); // 按上界降序
        }
    }
    
    public int solveKnapsack(int[] weights, int[] values, int capacity) {
        int n = weights.length;
        Item[] items = new Item[n];
        
        // 创建物品数组并按价值密度排序
        for (int i = 0; i < n; i++) {
            items[i] = new Item(weights[i], values[i]);
        }
        Arrays.sort(items);
        
        PriorityQueue<Node> queue = new PriorityQueue<>();
        Node u, v;
        
        // 创建虚拟根节点
        u = new Node(-1, 0, 0);
        u.bound = computeBound(u, items, capacity, n);
        queue.offer(u);
        
        int maxProfit = 0;
        
        while (!queue.isEmpty()) {
            u = queue.poll();
            
            // 如果当前节点的上界小于已知最大价值，剪枝
            if (u.bound <= maxProfit) {
                continue;
            }
            
            // 如果不是叶节点，扩展子节点
            if (u.level < n - 1) {
                // 扩展左子节点（选择下一个物品）
                v = new Node(u.level + 1, u.profit + items[u.level + 1].value, 
                            u.weight + items[u.level + 1].weight);
                
                if (v.weight <= capacity && v.profit > maxProfit) {
                    maxProfit = v.profit;
                }
                
                v.bound = computeBound(v, items, capacity, n);
                if (v.bound > maxProfit) {
                    queue.offer(v);
                }
                
                // 扩展右子节点（不选择下一个物品）
                v = new Node(u.level + 1, u.profit, u.weight);
                v.bound = computeBound(v, items, capacity, n);
                if (v.bound > maxProfit) {
                    queue.offer(v);
                }
            }
        }
        
        return maxProfit;
    }
    
    private double computeBound(Node node, Item[] items, int capacity, int n) {
        if (node.weight >= capacity) {
            return 0;
        }
        
        double bound = node.profit;
        int j = node.level + 1;
        int totalWeight = node.weight;
        
        // 贪心添加物品直到装满背包
        while (j < n && totalWeight + items[j].weight <= capacity) {
            totalWeight += items[j].weight;
            bound += items[j].value;
            j++;
        }
        
        // 如果还有剩余空间，添加部分物品
        if (j < n) {
            bound += (capacity - totalWeight) * items[j].ratio;
        }
        
        return bound;
    }
    
    public static void main(String[] args) {
        KnapsackBranchBound solver = new KnapsackBranchBound();
        int[] weights = {2, 3, 4, 5};
        int[] values = {3, 4, 5, 6};
        int capacity = 8;
        
        int maxProfit = solver.solveKnapsack(weights, values, capacity);
        System.out.println("最大价值: " + maxProfit);
    }
}