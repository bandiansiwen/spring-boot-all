package com.imp.all.demos.algorithm.tutorial.backtrack;

import java.util.*;

/**
 * @Author Daniel
 * @Date 2025/11/16/周日 18:47
 * @Description 组合问题
 **/
public class Combinations {
    public List<List<Integer>> combine(int n, int k) {
        List<List<Integer>> result = new ArrayList<>();
        backtrack(1, n, k, new ArrayList<>(), result);
        return result;
    }
    
    private void backtrack(int start, int n, int k, List<Integer> path, List<List<Integer>> result) {
        // 终止条件：路径长度等于k
        if (path.size() == k) {
            result.add(new ArrayList<>(path));
            return;
        }
        
        for (int i = start; i <= n; i++) {
            // 做选择
            path.add(i);
            
            // 递归，从i+1开始避免重复
            backtrack(i + 1, n, k, path, result);
            
            // 撤销选择
            path.remove(path.size() - 1);
        }
    }
    
    public static void main(String[] args) {
        Combinations solution = new Combinations();
        List<List<Integer>> result = solution.combine(4, 2);
        System.out.println(result);
        // 输出：[[1,2],[1,3],[1,4],[2,3],[2,4],[3,4]]
    }
}