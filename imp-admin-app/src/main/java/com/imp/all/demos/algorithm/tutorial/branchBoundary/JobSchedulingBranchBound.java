package com.imp.all.demos.algorithm.tutorial.branchBoundary;

import java.util.*;

/**
 * @Author Daniel
 * @Date 2025/11/16/周日 18:47
 * @Description 作业调度问题
 *
 * 有n个作业，每个作业有一个截止时间和利润。每个作业需要1个单位时间完成，在截止时间之前完成则获得相应利润。求如何安排作业使总利润最大。
 **/
public class JobSchedulingBranchBound {
    static class Job {
        int id;
        int deadline;
        int profit;
        
        Job(int id, int deadline, int profit) {
            this.id = id;
            this.deadline = deadline;
            this.profit = profit;
        }
    }
    
    static class Node implements Comparable<Node> {
        int level;
        boolean[] assigned;
        int profit;
        int bound;
        
        Node(int level, boolean[] assigned, int profit) {
            this.level = level;
            this.assigned = assigned.clone();
            this.profit = profit;
        }
        
        @Override
        public int compareTo(Node other) {
            return Integer.compare(other.bound, this.bound); // 按上界降序
        }
    }
    
    public int scheduleJobs(Job[] jobs) {
        int n = jobs.length;
        
        // 按利润降序排序
        Arrays.sort(jobs, (a, b) -> Integer.compare(b.profit, a.profit));
        
        // 找到最大截止时间
        int maxDeadline = 0;
        for (Job job : jobs) {
            maxDeadline = Math.max(maxDeadline, job.deadline);
        }
        
        PriorityQueue<Node> pq = new PriorityQueue<>();
        Node u = new Node(-1, new boolean[n], 0);
        u.bound = computeBound(u, jobs, n, maxDeadline);
        pq.offer(u);
        
        int maxProfit = 0;
        
        while (!pq.isEmpty()) {
            u = pq.poll();
            
            if (u.bound <= maxProfit) {
                continue;
            }
            
            if (u.level == n - 1) {
                if (u.profit > maxProfit) {
                    maxProfit = u.profit;
                }
                continue;
            }
            
            int level = u.level + 1;
            
            // 扩展：分配当前作业
            if (canAssign(u, jobs[level], maxDeadline)) {
                Node v = new Node(level, u.assigned, u.profit + jobs[level].profit);
                v.assigned[level] = true;
                v.bound = computeBound(v, jobs, n, maxDeadline);
                if (v.bound > maxProfit) {
                    pq.offer(v);
                }
            }
            
            // 扩展：不分配当前作业
            Node v = new Node(level, u.assigned, u.profit);
            v.bound = computeBound(v, jobs, n, maxDeadline);
            if (v.bound > maxProfit) {
                pq.offer(v);
            }
        }
        
        return maxProfit;
    }
    
    private boolean canAssign(Node node, Job job, int maxDeadline) {
        // 检查是否可以在截止时间前安排作业
        int timeSlot = Math.min(maxDeadline, job.deadline) - 1;
        while (timeSlot >= 0) {
            boolean conflict = false;
            for (int i = 0; i <= node.level; i++) {
                if (node.assigned[i] && jobs[i].deadline > timeSlot) {
                    // 这里需要更复杂的冲突检测
                    // 简化版本：假设每个时间槽只能安排一个作业
                    conflict = true;
                    break;
                }
            }
            if (!conflict) {
                return true;
            }
            timeSlot--;
        }
        return false;
    }
    
    private int computeBound(Node node, Job[] jobs, int n, int maxDeadline) {
        if (node.level == n - 1) {
            return node.profit;
        }
        
        int bound = node.profit;
        int time = countAssignedJobs(node);
        
        // 添加剩余作业中利润最大的
        for (int i = node.level + 1; i < n; i++) {
            if (time < maxDeadline) {
                bound += jobs[i].profit;
                time++;
            }
        }
        
        return bound;
    }
    
    private int countAssignedJobs(Node node) {
        int count = 0;
        for (boolean assigned : node.assigned) {
            if (assigned) count++;
        }
        return count;
    }
    
    private static Job[] jobs;
    
    public static void main(String[] args) {
        JobSchedulingBranchBound scheduler = new JobSchedulingBranchBound();
        jobs = new Job[]{
            new Job(1, 4, 70),
            new Job(2, 2, 60),
            new Job(3, 4, 50),
            new Job(4, 3, 40),
            new Job(5, 1, 30)
        };
        
        int maxProfit = scheduler.scheduleJobs(jobs);
        System.out.println("最大利润: " + maxProfit);
    }
}