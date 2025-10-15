package com.imp.all.demos.algorithm.dynamicProgramming;

import com.imp.all.demos.algorithm.tree.binary.TreeNode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Longlin
 * @date 2023/4/25 16:46
 * @description
 * 打家劫舍
 */
public class HouseRobber {

    /**
     * 牛客第 176 题 打家劫舍(一)
     * <a href="https://www.nowcoder.com/practice/c5fbf7325fbd4c0ea3d0c3ea6bc6cc79?tpId=196&tqId=39325&ru=/exam/oj">...</a>
     */
    private static int[] memo1;
    public static int rob(int[] nums) {
        memo1 = new int[nums.length];
        Arrays.fill(memo1, -1);
        return dp(nums, 0);
    }
    public static int dp(int[] nums, int start) {
        if (nums == null || start >= nums.length) {
            return 0;
        }
        // 避免重复计算
        if (memo1[start] != -1) {
            return memo1[start];
        }
        // 抢，然后去下下家
        int do_it = nums[start] + dp(nums, start + 2);
        // 不抢，然后去下家
        int not_do = dp(nums, start + 1);
        int res = Math.max(do_it, not_do);
        memo1[start] = res;
        return res;
    }

    /**
     * 牛客第 177 题 打家劫舍(二)
     * <a href="https://www.nowcoder.com/practice/a5c127769dd74a63ada7bff37d9c5815?tpId=196&tqId=39326&ru=/exam/oj">...</a>
     */
    public static int rob2(int[] nums) {
        int n = nums.length;
        if (n == 1) return nums[0];
        return Math.max(robRange(nums, 0, n - 2),
                robRange(nums, 1, n - 1));
    }

    // 仅计算闭区间 [start,end] 的最优结果
    public static int robRange(int[] nums, int start, int end) {
        int n = nums.length;
        int dp_i_1 = 0, dp_i_2 = 0;
        int dp_i = 0;
        for (int i = end; i >= start; i--) {
            dp_i = Math.max(dp_i_1, nums[i] + dp_i_2);
            dp_i_2 = dp_i_1;
            dp_i_1 = dp_i;
        }
        return dp_i;
    }

    /**
     * 牛客第 178 题 打家劫舍(三)
     * <a href="https://www.nowcoder.com/practice/82b6dce6a7634419b272ee4397e26d89?tpId=196&tqId=39327&ru=/exam/oj">...</a>
     */
    private static final Map<TreeNode, Integer> memo3 = new HashMap<>();
    public static int rob3(TreeNode root) {
        if (root == null) {
            return 0;
        }
        if (memo3.containsKey(root)) {
            return memo3.get(root);
        }

        // 抢，然后去下下家
        int do_it = root.val
                + (root.left==null ? 0 : rob3(root.left.left) + rob3(root.left.right))
                + (root.right==null ? 0 : rob3(root.right.left) + rob3(root.right.right));
        // 不抢，然后去下家
        int not_do = rob3(root.left) + rob3(root.right);

        int res = Math.max(do_it, not_do);
        memo3.put(root, res);
        return res;
    }

    public static void main(String[] args) {

        int[] nums = new int[]{3, 2, 1, 4};
        int rob1 = rob(nums);
        System.out.println(rob1);

        int i = rob2(nums);
        System.out.println(i);

        TreeNode d = new TreeNode(3, null, null);
        TreeNode c = new TreeNode(2, d, null);
        TreeNode b = new TreeNode(1, c, null);
        TreeNode a = new TreeNode(4, b, null);
        int rob = rob3(a);
        System.out.println(rob);
    }
}
