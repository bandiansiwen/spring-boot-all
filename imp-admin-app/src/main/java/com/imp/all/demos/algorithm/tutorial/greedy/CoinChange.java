package com.imp.all.demos.algorithm.tutorial.greedy;

import java.util.*;

/**
 * @Author Daniel
 * @Date 2025/11/16/周日 18:47
 * @Description 找零钱问题
 *
 * 找零钱问题（硬币数量最少）
 * 假设我们有面值为1,5,10,25的硬币，如何用最少的硬币凑出指定的金额？
 **/
public class CoinChange {
    public List<Integer> greedyCoinChange(int amount, int[] coins) {
        // 将硬币按面值从大到小排序
        Arrays.sort(coins);
        List<Integer> result = new ArrayList<>();
        
        // 从最大面值的硬币开始选择
        for (int i = coins.length - 1; i >= 0; i--) {
            while (amount >= coins[i]) {
                amount -= coins[i];
                result.add(coins[i]);
            }
        }
        
        return result;
    }
    
    public static void main(String[] args) {
        CoinChange solution = new CoinChange();
        int amount = 67;
        int[] coins = {1, 5, 10, 25};
        List<Integer> result = solution.greedyCoinChange(amount, coins);
        System.out.println("找零 " + amount + " 分需要的硬币: " + result);
        // 输出：找零 67 分需要的硬币: [25, 25, 10, 5, 1, 1]
    }
}