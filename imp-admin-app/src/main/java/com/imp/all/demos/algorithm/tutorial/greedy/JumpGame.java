package com.imp.all.demos.algorithm.tutorial.greedy;

/**
 * @Author Daniel
 * @Date 2025/11/16/周日 18:47
 * @Description 跳跃游戏
 **/
public class JumpGame {
    public boolean canJump(int[] nums) {
        int maxReach = 0;
        
        for (int i = 0; i < nums.length; i++) {
            if (i > maxReach) {
                return false; // 无法到达当前位置
            }
            maxReach = Math.max(maxReach, i + nums[i]);
            if (maxReach >= nums.length - 1) {
                return true; // 可以到达终点
            }
        }
        
        return false;
    }
    
    public int jump(int[] nums) {
        int jumps = 0;
        int currentEnd = 0;
        int farthest = 0;
        
        for (int i = 0; i < nums.length - 1; i++) {
            farthest = Math.max(farthest, i + nums[i]);
            
            if (i == currentEnd) {
                jumps++;
                currentEnd = farthest;
            }
        }
        
        return jumps;
    }
    
    public static void main(String[] args) {
        JumpGame game = new JumpGame();
        int[] nums1 = {2, 3, 1, 1, 4};
        System.out.println("能否到达终点: " + game.canJump(nums1)); // true
        
        int[] nums2 = {3, 2, 1, 0, 4};
        System.out.println("能否到达终点: " + game.canJump(nums2)); // false
        
        int[] nums3 = {2, 3, 1, 1, 4};
        System.out.println("最少跳跃次数: " + game.jump(nums3)); // 2
    }
}