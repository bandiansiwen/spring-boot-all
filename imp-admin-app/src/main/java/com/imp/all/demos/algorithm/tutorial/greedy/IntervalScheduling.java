package com.imp.all.demos.algorithm.tutorial.greedy;

import java.util.*;

/**
 * @Author Daniel
 * @Date 2025/11/16/周日 18:47
 * @Description 区间调度问题
 *
 * 活动选择问题
 * 假设有n个活动，每个活动有开始时间和结束时间，选择最多的互不冲突的活动。
 **/
public class IntervalScheduling {
    static class Interval {
        int start;
        int end;
        
        Interval(int start, int end) {
            this.start = start;
            this.end = end;
        }
        
        @Override
        public String toString() {
            return "[" + start + ", " + end + "]";
        }
    }
    
    public List<Interval> scheduleIntervals(Interval[] intervals) {
        // 按结束时间排序
        Arrays.sort(intervals, (a, b) -> a.end - b.end);
        
        List<Interval> result = new ArrayList<>();
        int lastEnd = Integer.MIN_VALUE;
        
        for (Interval interval : intervals) {
            // 如果当前区间不与已选区间冲突，则选择
            if (interval.start >= lastEnd) {
                result.add(interval);
                lastEnd = interval.end;
            }
        }
        
        return result;
    }
    
    public static void main(String[] args) {
        IntervalScheduling scheduler = new IntervalScheduling();
        Interval[] intervals = {
            new Interval(1, 3),
            new Interval(2, 4),
            new Interval(3, 5),
            new Interval(4, 6),
            new Interval(5, 7)
        };
        
        List<Interval> result = scheduler.scheduleIntervals(intervals);
        System.out.println("最多可以安排的活动: " + result);
        // 输出：最多可以安排的活动: [[1, 3], [3, 5], [5, 7]]
    }
}