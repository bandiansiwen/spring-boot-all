package com.imp.all.demos.algorithm.tutorial.backtrack;

/**
 * @Author Daniel
 * @Date 2025/11/16/周日 18:47
 * @Description 分割等和子集
 **/
public class PartitionEqualSubsetSum {
    public boolean canPartition(int[] nums) {
        int total = 0;
        for (int num : nums) {
            total += num;
        }
        
        // 如果总和是奇数，不可能平分
        if (total % 2 != 0) {
            return false;
        }
        
        int target = total / 2;
        return backtrack(nums, 0, 0, target);
    }
    
    private boolean backtrack(int[] nums, int index, int currentSum, int target) {
        // 终止条件
        if (currentSum == target) {
            return true;
        }
        if (currentSum > target || index >= nums.length) {
            return false;
        }
        
        // 选择当前数字
        if (backtrack(nums, index + 1, currentSum + nums[index], target)) {
            return true;
        }
        
        // 不选择当前数字
        if (backtrack(nums, index + 1, currentSum, target)) {
            return true;
        }
        
        return false;
    }
    
    // 优化版本：使用记忆化搜索
    public boolean canPartitionMemo(int[] nums) {
        int total = 0;
        for (int num : nums) {
            total += num;
        }
        
        if (total % 2 != 0) {
            return false;
        }
        
        int target = total / 2;
        Boolean[][] memo = new Boolean[nums.length][target + 1];
        return backtrackMemo(nums, 0, 0, target, memo);
    }
    
    private boolean backtrackMemo(int[] nums, int index, int currentSum, int target, Boolean[][] memo) {
        if (currentSum == target) {
            return true;
        }
        if (currentSum > target || index >= nums.length) {
            return false;
        }
        
        if (memo[index][currentSum] != null) {
            return memo[index][currentSum];
        }
        
        // 选择当前数字
        boolean select = backtrackMemo(nums, index + 1, currentSum + nums[index], target, memo);
        // 不选择当前数字
        boolean notSelect = backtrackMemo(nums, index + 1, currentSum, target, memo);
        
        memo[index][currentSum] = select || notSelect;
        return memo[index][currentSum];
    }
    
    public static void main(String[] args) {
        PartitionEqualSubsetSum solution = new PartitionEqualSubsetSum();
        int[] nums = {1, 5, 11, 5};
        System.out.println(solution.canPartitionMemo(nums)); // 输出：true
        
        int[] nums2 = {1, 2, 3, 5};
        System.out.println(solution.canPartitionMemo(nums2)); // 输出：false
    }
}