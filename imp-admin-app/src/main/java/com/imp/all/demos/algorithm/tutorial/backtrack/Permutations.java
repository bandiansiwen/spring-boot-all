package com.imp.all.demos.algorithm.tutorial.backtrack;

import java.util.*;

/**
 * @Author Daniel
 * @Date 2025/11/16/周日 18:47
 * @Description 全排列问题
 **/
public class Permutations {
    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        backtrack(nums, new ArrayList<>(), result);
        return result;
    }
    
    private void backtrack(int[] nums, List<Integer> path, List<List<Integer>> result) {
        // 终止条件：路径长度等于数组长度
        if (path.size() == nums.length) {
            result.add(new ArrayList<>(path));
            return;
        }
        
        for (int i = 0; i < nums.length; i++) {
            // 跳过已经选择过的元素
            if (path.contains(nums[i])) {
                continue;
            }
            
            // 做选择
            path.add(nums[i]);
            
            // 递归
            backtrack(nums, path, result);
            
            // 撤销选择（回溯）
            path.remove(path.size() - 1);
        }
    }
    
    public static void main(String[] args) {
        Permutations solution = new Permutations();
        int[] nums = {1, 2, 3};
        List<List<Integer>> result = solution.permute(nums);
        System.out.println(result);
        // 输出：[[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]
    }
}