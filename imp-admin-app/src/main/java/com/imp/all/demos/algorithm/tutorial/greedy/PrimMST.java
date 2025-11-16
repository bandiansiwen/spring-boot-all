package com.imp.all.demos.algorithm.tutorial.greedy;

import java.util.*;

/**
 * @Author Daniel
 * @Date 2025/11/16/周日 18:47
 * @Description 最小生成树 - Prim算法
 **/
public class PrimMST {
    public int minCostConnectPoints(int[][] points) {
        int n = points.length;
        boolean[] visited = new boolean[n];
        int[] minDist = new int[n];
        Arrays.fill(minDist, Integer.MAX_VALUE);
        minDist[0] = 0;
        
        int totalCost = 0;
        
        for (int i = 0; i < n; i++) {
            // 找到当前距离最小的未访问节点
            int u = -1;
            for (int j = 0; j < n; j++) {
                if (!visited[j] && (u == -1 || minDist[j] < minDist[u])) {
                    u = j;
                }
            }
            
            visited[u] = true;
            totalCost += minDist[u];
            
            // 更新相邻节点的最小距离
            for (int v = 0; v < n; v++) {
                if (!visited[v]) {
                    int dist = Math.abs(points[u][0] - points[v][0]) + 
                              Math.abs(points[u][1] - points[v][1]);
                    if (dist < minDist[v]) {
                        minDist[v] = dist;
                    }
                }
            }
        }
        
        return totalCost;
    }
    
    public static void main(String[] args) {
        PrimMST solution = new PrimMST();
        int[][] points = {{0,0}, {2,2}, {3,10}, {5,2}, {7,0}};
        int minCost = solution.minCostConnectPoints(points);
        System.out.println("最小连接成本: " + minCost);
    }
}