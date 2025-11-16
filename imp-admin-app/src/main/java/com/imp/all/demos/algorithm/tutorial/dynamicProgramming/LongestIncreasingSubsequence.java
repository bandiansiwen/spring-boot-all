package com.imp.all.demos.algorithm.tutorial.dynamicProgramming;

import java.util.Arrays;

/**
 * @Author Daniel
 * @Date 2025/11/16/周日 18:47
 * @Description 最长递增子序列
 *
 * 给定一个整数数组，找到最长递增子序列（LIS）的长度。子序列不要求连续。
 **/
public class LongestIncreasingSubsequence {
    public int lengthOfLIS(int[] nums) {
        if (nums.length == 0) return 0;
        
        // dp[i] 表示以 nums[i] 结尾的最长递增子序列长度
        int[] dp = new int[nums.length];
        Arrays.fill(dp, 1);
        int maxLength = 1;
        
        for (int i = 1; i < nums.length; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[i] > nums[j]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            maxLength = Math.max(maxLength, dp[i]);
        }
        
        return maxLength;
    }
    
    // 优化版本：二分查找
    public int lengthOfLISBinary(int[] nums) {
        int[] tails = new int[nums.length];
        int size = 0;
        
        for (int num : nums) {
            int left = 0, right = size;
            // 二分查找插入位置
            while (left < right) {
                int mid = left + (right - left) / 2;
                if (tails[mid] < num) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }
            tails[left] = num;
            if (left == size) size++;
        }
        
        return size;
    }
    
    public static void main(String[] args) {
        LongestIncreasingSubsequence lis = new LongestIncreasingSubsequence();
        int[] nums = {10, 9, 2, 5, 3, 7, 101, 18};
        System.out.println("最长递增子序列长度: " + lis.lengthOfLIS(nums));
        System.out.println("优化版本: " + lis.lengthOfLISBinary(nums));
    }
}