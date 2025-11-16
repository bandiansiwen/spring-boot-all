package com.imp.all.demos.algorithm.tutorial.branchBoundary;

import java.util.*;

/**
 * @Author Daniel
 * @Date 2025/11/16/周日 18:47
 * @Description 简单的分支限界框架
 *
 * 这是一个通用的分支限界框架，可以应用于各种优化问题。
 **/
public class BranchBoundFramework {
    static abstract class Problem {
        abstract double getBound();
        abstract boolean isFeasible();
        abstract boolean isComplete();
        abstract List<Problem> branch();
    }
    
    static class Solution {
        double value;
        Object solution;
        
        Solution(double value, Object solution) {
            this.value = value;
            this.solution = solution;
        }
    }
    
    public static Solution solve(Problem initialProblem) {
        PriorityQueue<Problem> queue = new PriorityQueue<>(
            (a, b) -> Double.compare(b.getBound(), a.getBound()) // 按上界降序
        );
        queue.offer(initialProblem);
        
        Solution bestSolution = null;
        
        while (!queue.isEmpty()) {
            Problem current = queue.poll();
            
            // 剪枝：如果当前上界不如已知最优解
            if (bestSolution != null && current.getBound() <= bestSolution.value) {
                continue;
            }
            
            if (current.isComplete()) {
                // 更新最优解
                double value = current.getBound(); // 假设上界就是实际值
                if (bestSolution == null || value > bestSolution.value) {
                    bestSolution = new Solution(value, current);
                }
                continue;
            }
            
            if (!current.isFeasible()) {
                continue;
            }
            
            // 分支
            for (Problem child : current.branch()) {
                if (bestSolution == null || child.getBound() > bestSolution.value) {
                    queue.offer(child);
                }
            }
        }
        
        return bestSolution;
    }
}