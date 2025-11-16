package com.imp.all.demos.algorithm.tutorial.dynamicProgramming;

import java.util.Arrays;

/**
 * @Author Daniel
 * @Date 2025/11/16/周日 18:47
 * @Description 硬币找零问题
 *
 * 给定不同面额的硬币和一个总金额。写出函数来计算可以凑成总金额的最少硬币数。如果无法凑成，返回-1。
 **/
public class CoinChange {
    // 最少硬币数
    public int coinChange(int[] coins, int amount) {
        // dp[i] 表示组成金额 i 需要的最少硬币数
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1);  // 初始化为最大值
        dp[0] = 0;  // 金额0需要0个硬币
        
        for (int i = 1; i <= amount; i++) {
            for (int coin : coins) {
                if (coin <= i) {
                    dp[i] = Math.min(dp[i], dp[i - coin] + 1);
                }
            }
        }
        
        return dp[amount] > amount ? -1 : dp[amount];
    }
    
    // 组合数（硬币顺序无关）
    public int change(int amount, int[] coins) {
        // dp[i] 表示组成金额 i 的组合数
        int[] dp = new int[amount + 1];
        dp[0] = 1;  // 金额0有1种组合：不选任何硬币
        
        for (int coin : coins) {
            for (int i = coin; i <= amount; i++) {
                dp[i] += dp[i - coin];
            }
        }
        
        return dp[amount];
    }
    
    public static void main(String[] args) {
        CoinChange cc = new CoinChange();
        int[] coins = {1, 2, 5};
        int amount = 11;
        System.out.println("最少硬币数: " + cc.coinChange(coins, amount));
        System.out.println("组合数: " + cc.change(amount, coins));
    }
}