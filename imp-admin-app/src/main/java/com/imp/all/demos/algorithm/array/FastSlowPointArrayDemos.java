package com.imp.all.demos.algorithm.array;

import java.util.Arrays;

/**
 * @author Longlin
 * @date 2023/4/19 18:02
 * @description
 */
public class FastSlowPointArrayDemos {

    /**
     * 力扣第 26 题「 删除有序数组中的重复项」
     *  <a href="https://leetcode.cn/problems/remove-duplicates-from-sorted-array/">...</a>
     */
    public static int removeDuplicates(int[] nums) {
        if (nums.length == 0) return 0;

        int slow=0, fast=0;
        while (fast < nums.length) {
            if (nums[fast] != nums[slow]) {
                slow++;
                // 维护 nums[0..slow] 无重复
                nums[slow] = nums[fast];
            }
            fast++;
        }
        // 数组长度为索引 + 1
        return slow + 1;
    }

    /**
     * 力扣第 27 题「 移除元素」
     * <a href="https://leetcode.cn/problems/remove-element/">...</a>
     */
    public static int removeElement(int[] nums, int val) {
        int fast = 0, slow = 0;
        while (fast < nums.length) {
            if (nums[fast] != val) {
                // 这里和有序数组去重的解法有一个细节差异，我们这里是先给 nums[slow] 赋值然后再给 slow++，这样可以保证 nums[0..slow-1] 是不包含值为 val 的元素的，最后的结果数组长度就是 slow。
                nums[slow] = nums[fast];
                slow++;
            }
            fast++;
        }
        return slow;
    }

    /**
     * 力扣第 283 题「 移动零」
     * <a href="https://leetcode.cn/problems/move-zeroes/">...</a>
     */
    public static void moveZeroes(int[] nums) {
        // 去除 nums 中的所有 0，返回不含 0 的数组长度
        int p = removeElement(nums, 0);
        // 将 nums[p..] 的元素赋值为 0
        for (; p < nums.length; p++) {
            nums[p] = 0;
        }
    }

    public static void main(String[] args) {

        int[] nums = new int[]{1,2,2,3,3,5,7};
        int i = removeDuplicates(nums);
        System.out.println(i);
        System.out.println(Arrays.toString(nums));

        int[] nums2 = new int[]{0,1,3,0,2};
        moveZeroes(nums2);
        System.out.println(Arrays.toString(nums2));
    }
}
