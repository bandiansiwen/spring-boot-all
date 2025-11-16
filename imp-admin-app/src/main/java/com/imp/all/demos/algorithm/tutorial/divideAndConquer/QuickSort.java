package com.imp.all.demos.algorithm.tutorial.divideAndConquer;

import java.util.Arrays;

/**
 * @Author Daniel
 * @Date 2025/11/16/周日 18:47
 * @Description 快速排序
 **/
public class QuickSort {
    public void quickSort(int[] arr) {
        if (arr == null || arr.length <= 1) {
            return;
        }
        quickSort(arr, 0, arr.length - 1);
    }
    
    private void quickSort(int[] arr, int left, int right) {
        // 分解：递归终止条件
        if (left >= right) {
            return;
        }
        
        // 分解：分区操作，返回基准值位置
        int pivotIndex = partition(arr, left, right);
        
        // 解决：递归排序基准值左右两部分
        quickSort(arr, left, pivotIndex - 1);
        quickSort(arr, pivotIndex + 1, right);
    }
    
    private int partition(int[] arr, int left, int right) {
        // 选择最右边的元素作为基准值
        int pivot = arr[right];
        int i = left - 1; // 小于基准值的元素的边界
        
        for (int j = left; j < right; j++) {
            if (arr[j] <= pivot) {
                i++;
                swap(arr, i, j);
            }
        }
        
        // 将基准值放到正确位置
        swap(arr, i + 1, right);
        return i + 1;
    }
    
    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
    
    public static void main(String[] args) {
        QuickSort sorter = new QuickSort();
        int[] arr = {64, 34, 25, 12, 22, 11, 90};
        System.out.println("排序前: " + Arrays.toString(arr));
        sorter.quickSort(arr);
        System.out.println("排序后: " + Arrays.toString(arr));
    }
}