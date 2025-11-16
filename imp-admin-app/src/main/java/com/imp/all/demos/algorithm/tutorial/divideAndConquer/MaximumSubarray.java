package com.imp.all.demos.algorithm.tutorial.divideAndConquer;

/**
 * @Author Daniel
 * @Date 2025/11/16/周日 18:47
 * @Description 最大子数组和
 **/
public class MaximumSubarray {
    public int maxSubArray(int[] nums) {
        return maxSubArrayDivide(nums, 0, nums.length - 1);
    }
    
    private int maxSubArrayDivide(int[] nums, int left, int right) {
        // 分解：递归终止条件
        if (left == right) {
            return nums[left];
        }
        
        // 分解：找到中间点
        int mid = left + (right - left) / 2;
        
        // 解决：递归求解左右两半的最大子数组和
        int leftMax = maxSubArrayDivide(nums, left, mid);
        int rightMax = maxSubArrayDivide(nums, mid + 1, right);
        
        // 解决：计算跨越中间的最大子数组和
        int crossMax = maxCrossingSum(nums, left, mid, right);
        
        // 合并：返回三者中的最大值
        return Math.max(Math.max(leftMax, rightMax), crossMax);
    }
    
    private int maxCrossingSum(int[] nums, int left, int mid, int right) {
        // 计算左半部分的最大后缀和
        int leftSum = Integer.MIN_VALUE;
        int sum = 0;
        for (int i = mid; i >= left; i--) {
            sum += nums[i];
            leftSum = Math.max(leftSum, sum);
        }
        
        // 计算右半部分的最大前缀和
        int rightSum = Integer.MIN_VALUE;
        sum = 0;
        for (int i = mid + 1; i <= right; i++) {
            sum += nums[i];
            rightSum = Math.max(rightSum, sum);
        }
        
        return leftSum + rightSum;
    }
    
    public static void main(String[] args) {
        MaximumSubarray solution = new MaximumSubarray();
        int[] nums = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        System.out.println("最大子数组和: " + solution.maxSubArray(nums)); // 输出: 6
    }
}