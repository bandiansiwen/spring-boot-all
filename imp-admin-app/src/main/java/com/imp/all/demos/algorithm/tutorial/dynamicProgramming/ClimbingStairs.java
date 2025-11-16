package com.imp.all.demos.algorithm.tutorial.dynamicProgramming;

/**
 * @Author Daniel
 * @Date 2025/11/16/周日 18:47
 * @Description 爬楼梯问题
 *
 * 假设你正在爬楼梯。需要n阶你才能到达楼顶。每次你可以爬1或2个台阶。你有多少种不同的方法可以爬到楼顶？
 **/
public class ClimbingStairs {
    public int climbStairs(int n) {
        if (n <= 2) return n;
        
        int[] dp = new int[n + 1];
        dp[1] = 1;  // 1阶：1种方式
        dp[2] = 2;  // 2阶：2种方式 (1+1, 2)
        
        for (int i = 3; i <= n; i++) {
            // 状态转移方程：dp[i] = dp[i-1] + dp[i-2]
            dp[i] = dp[i - 1] + dp[i - 2];
        }
        
        return dp[n];
    }
    
    // 空间优化版本
    public int climbStairsOptimized(int n) {
        if (n <= 2) return n;
        
        int prev2 = 1;  // dp[i-2]
        int prev1 = 2;  // dp[i-1]
        
        for (int i = 3; i <= n; i++) {
            int current = prev1 + prev2;
            prev2 = prev1;
            prev1 = current;
        }
        
        return prev1;
    }
    
    public static void main(String[] args) {
        ClimbingStairs cs = new ClimbingStairs();
        int n = 5;
        System.out.println("爬 " + n + " 阶楼梯的方法数: " + cs.climbStairs(n));
    }
}