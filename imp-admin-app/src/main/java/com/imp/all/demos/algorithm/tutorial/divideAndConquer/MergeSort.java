package com.imp.all.demos.algorithm.tutorial.divideAndConquer;

import java.util.Arrays;

/**
 * @Author Daniel
 * @Date 2025/11/16/周日 18:47
 * @Description 归并排序
 **/
public class MergeSort {
    public void mergeSort(int[] arr) {
        if (arr == null || arr.length <= 1) {
            return;
        }
        int[] temp = new int[arr.length];
        mergeSort(arr, 0, arr.length - 1, temp);
    }
    
    private void mergeSort(int[] arr, int left, int right, int[] temp) {
        // 分解：递归终止条件
        if (left >= right) {
            return;
        }
        
        // 分解：找到中间点
        int mid = left + (right - left) / 2;
        
        // 解决：递归排序左右两半
        mergeSort(arr, left, mid, temp);
        mergeSort(arr, mid + 1, right, temp);
        
        // 合并：合并两个有序数组
        merge(arr, left, mid, right, temp);
    }
    
    private void merge(int[] arr, int left, int mid, int right, int[] temp) {
        int i = left;       // 左半部分起始索引
        int j = mid + 1;    // 右半部分起始索引
        int k = 0;          // 临时数组索引
        
        // 合并两个有序数组
        while (i <= mid && j <= right) {
            if (arr[i] <= arr[j]) {
                temp[k++] = arr[i++];
            } else {
                temp[k++] = arr[j++];
            }
        }
        
        // 复制左半部分剩余元素
        while (i <= mid) {
            temp[k++] = arr[i++];
        }
        
        // 复制右半部分剩余元素
        while (j <= right) {
            temp[k++] = arr[j++];
        }
        
        // 将临时数组复制回原数组
        k = 0;
        while (left <= right) {
            arr[left++] = temp[k++];
        }
    }
    
    public static void main(String[] args) {
        MergeSort sorter = new MergeSort();
        int[] arr = {38, 27, 43, 3, 9, 82, 10};
        System.out.println("排序前: " + Arrays.toString(arr));
        sorter.mergeSort(arr);
        System.out.println("排序后: " + Arrays.toString(arr));
    }
}