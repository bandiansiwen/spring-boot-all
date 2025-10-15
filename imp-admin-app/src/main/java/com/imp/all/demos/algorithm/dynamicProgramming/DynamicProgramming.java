package com.imp.all.demos.algorithm.dynamicProgramming;

import java.util.Arrays;

/**
 * @author Longlin
 * @date 2023/4/21 16:06
 * @description
 * 动态规划
 */
public class DynamicProgramming {

    /**
     * 力扣第 322 题「 零钱兑换」
     * <a href="https://leetcode.cn/problems/coin-change/">...</a>
     *
     * 状态：目标金额 amount
     * 选择：coins 数组中所有硬币面额
     * 函数定义：凑出金额 amount，则至少需要 coinChange(int[] coins, int amount) 枚硬币
     * basecase：amount 等于 0 时，需要 0 枚硬币；amount < 0 时，不可能凑出
     */
    public static int[] memo;
    public static int coinChange(int[] coins, int amount) {
        memo = new int[amount + 1];
        // 备忘录初始化为一个不会被取到的特殊值，代表还未被计算
        Arrays.fill(memo, -666);

        // 题目要求的最终结果是 dp(amount)
        return dp(coins, amount);
    }

    // 定义：要凑出金额 n，至少要 dp(coins, amount) 个硬币
    public static int dp(int[] coins, int amount) {
        // base case
         if (amount == 0) {
            return 0;
        }
        if (amount < 0) {
            return -1;
        }

        // 查备忘录，防止重复计算
        if (memo[amount] != -666) {
            return memo[amount];
        }

        int res = Integer.MAX_VALUE;
        for (int coin: coins) {
            // 计算子问题的结果
            int subProblem = dp(coins, amount-coin);
            // 子问题无解则跳过
            if (subProblem == -1) {
                continue;
            }
            // 在子问题中选择最优解，然后加一
            res = Math.min(res, subProblem + 1);
        }

        // 把计算结果存入备忘录
        memo[amount] = (res == Integer.MAX_VALUE) ? -1 : res;
        return memo[amount];
    }

    /**
     * dp 数组的迭代解法
     */
    public static int coinChange2(int[] coins, int amount) {
        int[] dp = new int[amount+1];
        // 数组大小为 amount + 1，初始值也为 amount + 1
        Arrays.fill(dp, amount + 1);

        // base case
        dp[0] = 0;
        // 外层 for 循环在遍历所有状态的所有取值
        for (int i = 1; i < dp.length; i++) {
            // 内层 for 循环在求所有选择的最小值
            for (int coin: coins) {
                // 子问题无解，跳过
                if (i - coin < 0) {
                    continue;
                }
                dp[i] = Math.min(dp[i], 1 + dp[i - coin]);
            }
        }
        return (dp[amount] == amount+1) ? -1 : dp[amount];
    }

    public static void main(String[] args) {
        int[] coins = {1,2,3,5};
        int amount = 18;
        int i = coinChange(coins, amount);
        System.out.println(i);

        int[] coins2 = {2};
        int amount2 = 3;
        int i1 = coinChange2(coins2, amount2);
        System.out.println(i1);
    }
}
