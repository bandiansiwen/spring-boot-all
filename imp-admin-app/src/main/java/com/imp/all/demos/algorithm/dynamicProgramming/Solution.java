package com.imp.all.demos.algorithm.dynamicProgramming;

/**
 * @author Longlin
 * @date 2023/4/25 22:23
 * @description
 */
public class Solution {

    public static int uniquePaths(int m, int n) {
        int[][]f = new int[m][n];
        int i,j;
        for (i = 0; i < m; i++) {
            for (j = 0; j < n; j++) {
                if (i==0 || j==0) {
                    f[i][j] = 1;
                }
                else {
                    f[i][j] = f[i-1][j] + f[i][j-1];
                }
            }
        }
        return f[m-1][n-1];
    }

    public static void main(String[] args) {
        int i = uniquePaths(4, 4);
        System.out.println(i);
    }
}
