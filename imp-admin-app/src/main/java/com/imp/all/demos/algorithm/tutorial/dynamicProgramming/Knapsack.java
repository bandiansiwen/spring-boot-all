package com.imp.all.demos.algorithm.tutorial.dynamicProgramming;

/**
 * @Author Daniel
 * @Date 2025/11/16/周日 18:47
 * @Description 0-1背包问题
 *
 * 给定一组物品，每种物品都有自己的重量和价值，在限定的总重量内，我们如何选择，才能使得物品的总价值最高。
 **/
public class Knapsack {
    public int knapsack01(int[] weights, int[] values, int capacity) {
        int n = weights.length;
        // dp[i][w] 表示前i个物品，容量为w时的最大价值
        int[][] dp = new int[n + 1][capacity + 1];
        
        for (int i = 1; i <= n; i++) {
            for (int w = 1; w <= capacity; w++) {
                if (weights[i - 1] <= w) {
                    // 选择：放入或不放入当前物品
                    dp[i][w] = Math.max(
                        dp[i - 1][w],  // 不放入
                        dp[i - 1][w - weights[i - 1]] + values[i - 1]  // 放入
                    );
                } else {
                    // 当前物品太重，无法放入
                    dp[i][w] = dp[i - 1][w];
                }
            }
        }
        
        return dp[n][capacity];
    }
    
    // 空间优化版本（一维数组）
    public int knapsack01Optimized(int[] weights, int[] values, int capacity) {
        int n = weights.length;
        int[] dp = new int[capacity + 1];
        
        for (int i = 0; i < n; i++) {
            // 倒序遍历，避免覆盖前一状态
            for (int w = capacity; w >= weights[i]; w--) {
                dp[w] = Math.max(dp[w], dp[w - weights[i]] + values[i]);
            }
        }
        
        return dp[capacity];
    }
    
    public static void main(String[] args) {
        Knapsack ks = new Knapsack();
        int[] weights = {2, 3, 4, 5};
        int[] values = {3, 4, 5, 6};
        int capacity = 8;
        
        System.out.println("0-1背包最大价值: " + 
            ks.knapsack01(weights, values, capacity));
    }
}