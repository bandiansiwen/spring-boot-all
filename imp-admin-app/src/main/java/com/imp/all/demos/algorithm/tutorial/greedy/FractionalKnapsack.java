package com.imp.all.demos.algorithm.tutorial.greedy;

import java.util.*;

/**
 * @Author Daniel
 * @Date 2025/11/16/周日 18:47
 * @Description 背包问题（分数背包）
 *
 * 背包问题（分数背包）
 * 在分数背包问题中，物品可以分割，如何选择使得背包中的总价值最大？
 **/
public class FractionalKnapsack {
    static class Item {
        int weight;
        int value;
        
        Item(int weight, int value) {
            this.weight = weight;
            this.value = value;
        }
        
        double getValuePerWeight() {
            return (double) value / weight;
        }
    }
    
    public double getMaxValue(int capacity, Item[] items) {
        // 按单位价值从高到低排序
        Arrays.sort(items, (a, b) -> 
            Double.compare(b.getValuePerWeight(), a.getValuePerWeight()));
        
        double totalValue = 0;
        int remainingCapacity = capacity;
        
        for (Item item : items) {
            if (remainingCapacity >= item.weight) {
                // 可以装下整个物品
                totalValue += item.value;
                remainingCapacity -= item.weight;
            } else {
                // 只能装下部分物品
                totalValue += item.getValuePerWeight() * remainingCapacity;
                break;
            }
        }
        
        return totalValue;
    }
    
    public static void main(String[] args) {
        FractionalKnapsack knapsack = new FractionalKnapsack();
        Item[] items = {
            new Item(10, 60),  // 价值密度: 6.0
            new Item(20, 100), // 价值密度: 5.0
            new Item(30, 120)  // 价值密度: 4.0
        };
        int capacity = 50;
        
        double maxValue = knapsack.getMaxValue(capacity, items);
        System.out.println("背包最大价值: " + maxValue);
        // 输出：背包最大价值: 240.0
    }
}