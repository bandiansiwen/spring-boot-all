package com.imp.all.demos.algorithm.tutorial.dynamicProgramming;

/**
 * @Author Daniel
 * @Date 2025/11/16/周日 18:47
 * @Description 最长公共子序列
 *
 * 给定两个字符串，求它们的最长公共子序列（LCS）的长度。子序列不要求连续。
 **/
public class LongestCommonSubsequence {
    public int longestCommonSubsequence(String text1, String text2) {
        int m = text1.length(), n = text2.length();
        // dp[i][j] 表示 text1[0..i-1] 和 text2[0..j-1] 的LCS长度
        int[][] dp = new int[m + 1][n + 1];
        
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    // 字符匹配，长度+1
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    // 字符不匹配，取最大值
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        
        return dp[m][n];
    }
    
    // 重构LCS
    public String getLCS(String text1, String text2) {
        int m = text1.length(), n = text2.length();
        int[][] dp = new int[m + 1][n + 1];
        
        // 填充DP表
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        
        // 回溯构建LCS
        StringBuilder lcs = new StringBuilder();
        int i = m, j = n;
        while (i > 0 && j > 0) {
            if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                lcs.append(text1.charAt(i - 1));
                i--;
                j--;
            } else if (dp[i - 1][j] > dp[i][j - 1]) {
                i--;
            } else {
                j--;
            }
        }
        
        return lcs.reverse().toString();
    }
    
    public static void main(String[] args) {
        LongestCommonSubsequence lcs = new LongestCommonSubsequence();
        String text1 = "abcde", text2 = "ace";
        System.out.println("LCS长度: " + lcs.longestCommonSubsequence(text1, text2));
        System.out.println("LCS: " + lcs.getLCS(text1, text2));
    }
}