package com.imp.all.demos.algorithm.tutorial.divideAndConquer;

/**
 * @Author Daniel
 * @Date 2025/11/16/周日 18:47
 * @Description 二分查找
 **/
public class BinarySearch {
    public int binarySearch(int[] arr, int target) {
        return binarySearch(arr, 0, arr.length - 1, target);
    }
    
    private int binarySearch(int[] arr, int left, int right, int target) {
        // 分解：递归终止条件
        if (left > right) {
            return -1; // 未找到
        }
        
        // 分解：找到中间点
        int mid = left + (right - left) / 2;
        
        // 解决：比较并决定搜索方向
        if (arr[mid] == target) {
            return mid; // 找到目标
        } else if (arr[mid] > target) {
            // 在左半部分搜索
            return binarySearch(arr, left, mid - 1, target);
        } else {
            // 在右半部分搜索
            return binarySearch(arr, mid + 1, right, target);
        }
    }
    
    public static void main(String[] args) {
        BinarySearch searcher = new BinarySearch();
        int[] arr = {1, 3, 5, 7, 9, 11, 13, 15, 17, 19};
        int target = 13;
        int result = searcher.binarySearch(arr, target);
        System.out.println("目标 " + target + " 的索引: " + result);
    }
}