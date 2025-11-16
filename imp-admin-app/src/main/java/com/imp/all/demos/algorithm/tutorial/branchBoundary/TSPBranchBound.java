package com.imp.all.demos.algorithm.tutorial.branchBoundary;

import java.util.*;

/**
 * @Author Daniel
 * @Date 2025/11/16/周日 18:47
 * @Description 旅行商问题（TSP）
 *
 * 给定一系列城市和每对城市之间的距离，求解访问每一座城市一次并回到起始城市的最短回路。
 **/
public class TSPBranchBound {
    static class Node implements Comparable<Node> {
        int level;          // 当前层级（已访问的城市数）
        int[] path;         // 当前路径
        int cost;           // 当前路径成本
        int bound;          // 下界估计
        boolean[] visited;  // 访问标记
        
        Node(int level, int[] path, int cost, int bound, boolean[] visited) {
            this.level = level;
            this.path = path.clone();
            this.cost = cost;
            this.bound = bound;
            this.visited = visited.clone();
        }
        
        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.bound, other.bound); // 按下界升序
        }
    }
    
    public int solveTSP(int[][] graph) {
        int n = graph.length;
        PriorityQueue<Node> pq = new PriorityQueue<>();
        
        // 计算初始下界
        int initialBound = computeInitialBound(graph);
        
        // 初始化根节点
        boolean[] visited = new boolean[n];
        visited[0] = true;
        int[] initialPath = new int[n + 1];
        initialPath[0] = 0;
        
        Node root = new Node(0, initialPath, 0, initialBound, visited);
        pq.offer(root);
        
        int minCost = Integer.MAX_VALUE;
        int[] bestPath = null;
        
        while (!pq.isEmpty()) {
            Node current = pq.poll();
            
            // 如果当前下界已经大于已知最小成本，剪枝
            if (current.bound >= minCost) {
                continue;
            }
            
            // 如果已经形成完整路径
            if (current.level == n - 1) {
                // 添加返回起点的边
                int finalCost = current.cost + graph[current.path[current.level]][0];
                if (finalCost < minCost) {
                    minCost = finalCost;
                    bestPath = current.path.clone();
                    bestPath[n] = 0;
                }
                continue;
            }
            
            // 扩展子节点
            for (int i = 0; i < n; i++) {
                if (!current.visited[i] && graph[current.path[current.level]][i] != 0) {
                    Node child = createChildNode(current, i, graph);
                    if (child.bound < minCost) {
                        pq.offer(child);
                    }
                }
            }
        }
        
        if (bestPath != null) {
            System.out.println("最优路径: " + Arrays.toString(bestPath));
        }
        
        return minCost;
    }
    
    private int computeInitialBound(int[][] graph) {
        int n = graph.length;
        int bound = 0;
        
        for (int i = 0; i < n; i++) {
            int min1 = Integer.MAX_VALUE, min2 = Integer.MAX_VALUE;
            
            // 找到每个城市的两个最小出边
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    if (graph[i][j] < min1) {
                        min2 = min1;
                        min1 = graph[i][j];
                    } else if (graph[i][j] < min2) {
                        min2 = graph[i][j];
                    }
                }
            }
            
            bound += (min1 + min2);
        }
        
        return (bound + 1) / 2; // 取上整
    }
    
    private Node createChildNode(Node parent, int nextCity, int[][] graph) {
        int n = graph.length;
        int level = parent.level + 1;
        
        // 创建新路径
        int[] newPath = parent.path.clone();
        newPath[level] = nextCity;
        
        // 更新访问标记
        boolean[] newVisited = parent.visited.clone();
        newVisited[nextCity] = true;
        
        // 计算新成本
        int newCost = parent.cost + graph[parent.path[parent.level]][nextCity];
        
        // 计算新下界
        int newBound = computeBound(newPath, level, newCost, graph);
        
        return new Node(level, newPath, newCost, newBound, newVisited);
    }
    
    private int computeBound(int[] path, int level, int cost, int[][] graph) {
        int n = graph.length;
        int bound = cost;
        
        // 对于未访问的城市，加上最小两条出边的平均值
        for (int i = 0; i < n; i++) {
            if (!isVisitedInPath(path, level, i)) {
                int min1 = Integer.MAX_VALUE, min2 = Integer.MAX_VALUE;
                
                for (int j = 0; j < n; j++) {
                    if (i != j && !isVisitedInPath(path, level, j)) {
                        if (graph[i][j] < min1) {
                            min2 = min1;
                            min1 = graph[i][j];
                        } else if (graph[i][j] < min2) {
                            min2 = graph[i][j];
                        }
                    }
                }
                
                bound += (min1 + min2);
            }
        }
        
        return (bound + 1) / 2;
    }
    
    private boolean isVisitedInPath(int[] path, int level, int city) {
        for (int i = 0; i <= level; i++) {
            if (path[i] == city) {
                return true;
            }
        }
        return false;
    }
    
    public static void main(String[] args) {
        TSPBranchBound solver = new TSPBranchBound();
        int[][] graph = {
            {0, 10, 15, 20},
            {10, 0, 35, 25},
            {15, 35, 0, 30},
            {20, 25, 30, 0}
        };
        
        int minCost = solver.solveTSP(graph);
        System.out.println("最小成本: " + minCost);
    }
}