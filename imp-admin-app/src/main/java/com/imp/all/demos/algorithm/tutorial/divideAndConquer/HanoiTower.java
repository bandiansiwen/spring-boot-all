package com.imp.all.demos.algorithm.tutorial.divideAndConquer;

/**
 * @Author Daniel
 * @Date 2025/11/16/周日 18:47
 * @Description 汉诺塔问题
 **/
public class HanoiTower {
    public void solveHanoi(int n, char fromRod, char toRod, char auxRod) {
        // 分解：递归终止条件
        if (n == 1) {
            System.out.println("移动盘子 1 从 " + fromRod + " 到 " + toRod);
            return;
        }
        
        // 解决：将n-1个盘子从起始柱移动到辅助柱
        solveHanoi(n - 1, fromRod, auxRod, toRod);
        
        // 解决：移动最大的盘子到目标柱
        System.out.println("移动盘子 " + n + " 从 " + fromRod + " 到 " + toRod);
        
        // 解决：将n-1个盘子从辅助柱移动到目标柱
        solveHanoi(n - 1, auxRod, toRod, fromRod);
    }
    
    public static void main(String[] args) {
        HanoiTower hanoi = new HanoiTower();
        int n = 3; // 盘子数量
        hanoi.solveHanoi(n, 'A', 'C', 'B');
    }
}