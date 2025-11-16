package com.imp.all.demos.algorithm.tutorial.dynamicProgramming;

import java.util.Arrays;

/**
 * @Author Daniel
 * @Date 2025/11/16/周日 18:47
 * @Description 斐波那契数列
 *
 * 斐波那契数列的定义如下：F(0)=0，F(1)=1，F(n)=F(n-1)+F(n-2)（n≥2）。给定n，求F(n)。
 **/
public class Fibonacci {
    // 方法1：递归（暴力解法）
    public int fibRecursive(int n) {
        if (n <= 1) return n;
        return fibRecursive(n - 1) + fibRecursive(n - 2);
    }
    
    // 方法2：记忆化搜索（自顶向下）
    public int fibMemo(int n) {
        int[] memo = new int[n + 1];
        Arrays.fill(memo, -1);
        return fibHelper(n, memo);
    }
    
    private int fibHelper(int n, int[] memo) {
        if (n <= 1) return n;
        if (memo[n] != -1) return memo[n];
        memo[n] = fibHelper(n - 1, memo) + fibHelper(n - 2, memo);
        return memo[n];
    }
    
    // 方法3：动态规划（自底向上）
    public int fibDP(int n) {
        if (n <= 1) return n;
        
        int[] dp = new int[n + 1];
        dp[0] = 0;
        dp[1] = 1;
        
        for (int i = 2; i <= n; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }
        
        return dp[n];
    }
    
    // 方法4：空间优化
    public int fibOptimized(int n) {
        if (n <= 1) return n;
        
        int prev2 = 0;  // dp[i-2]
        int prev1 = 1;  // dp[i-1]
        
        for (int i = 2; i <= n; i++) {
            int current = prev1 + prev2;
            prev2 = prev1;
            prev1 = current;
        }
        
        return prev1;
    }
    
    public static void main(String[] args) {
        Fibonacci fib = new Fibonacci();
        int n = 10;
        System.out.println("斐波那契数列第" + n + "项:");
        System.out.println("递归: " + fib.fibRecursive(n));
        System.out.println("记忆化: " + fib.fibMemo(n));
        System.out.println("动态规划: " + fib.fibDP(n));
        System.out.println("空间优化: " + fib.fibOptimized(n));
    }
}