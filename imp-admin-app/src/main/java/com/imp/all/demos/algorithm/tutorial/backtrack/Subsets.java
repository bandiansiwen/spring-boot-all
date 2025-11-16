package com.imp.all.demos.algorithm.tutorial.backtrack;

import java.util.*;

/**
 * @Author Daniel
 * @Date 2025/11/16/周日 18:47
 * @Description 子集问题
 **/
public class Subsets {
    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        backtrack(0, nums, new ArrayList<>(), result);
        return result;
    }
    
    private void backtrack(int start, int[] nums, List<Integer> path, List<List<Integer>> result) {
        // 每个节点都是结果，不需要终止条件
        result.add(new ArrayList<>(path));
        
        for (int i = start; i < nums.length; i++) {
            // 做选择
            path.add(nums[i]);
            
            // 递归
            backtrack(i + 1, nums, path, result);
            
            // 撤销选择
            path.remove(path.size() - 1);
        }
    }
    
    public static void main(String[] args) {
        Subsets solution = new Subsets();
        int[] nums = {1, 2, 3};
        List<List<Integer>> result = solution.subsets(nums);
        System.out.println(result);
        // 输出：[[],[1],[1,2],[1,2,3],[1,3],[2],[2,3],[3]]
    }
}