package com.imp.all.demos.algorithm.array;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author Longlin
 * @date 2023/12/25 23:40
 * @description
 *
 * 力扣第 547 题「 省份数量 」
 * https://leetcode.cn/problems/number-of-provinces/
 */
@Slf4j
public class ProvinceCount {

    /**
     * BFS 解题思路
     * @param isConnected
     * @return
     */
    public static int findCircleNum(int[][] isConnected) {
        int n = isConnected.length;
        int res = 0;

        // 记录已访问城市
        List<Integer> visited = new ArrayList<>();
        // 记录路径
        Queue<Integer> queue = new LinkedList<>();

        for (int i = 0; i < n; i++) {
            // 未访问城市入队
            if (!visited.contains(i)) {
                queue.offer(i);
                while (!queue.isEmpty()) {
                    int cur = queue.poll();
                    // 当前城市已访问
                    visited.add(cur);
                    // 寻找连接城市
                    for (int j = 0; j < n; j++) {
                        if (isConnected[cur][j] == 1 && !visited.contains(j)) {
                            // 连接城市入队
                            queue.offer(j);
                        }
                    }
                }
                res++;
            }
        }
        return res;
    }

    public static void main(String[] args) {
        int[][] isConnected = {
                {1,1,0},
                {1,1,0},
                {0,0,1}
        };

        int circleNum = findCircleNum(isConnected);

        log.info("省份数量为：{}", circleNum);
    }
}
