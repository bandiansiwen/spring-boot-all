package com.imp.all.demos.algorithm.tutorial.dynamicProgramming;

/**
 * @Author Daniel
 * @Date 2025/11/16/周日 18:47
 * @Description 编辑距离
 *
 * 给定两个单词word1和word2，计算出将word1转换成word2所使用的最少操作数。操作包括插入、删除、替换。
 **/
public class EditDistance {
    public int minDistance(String word1, String word2) {
        int m = word1.length(), n = word2.length();
        // dp[i][j] 表示 word1[0..i-1] 转换为 word2[0..j-1] 的最小操作数
        int[][] dp = new int[m + 1][n + 1];
        
        // 初始化边界条件
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;  // 删除所有字符
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;  // 插入所有字符
        }
        
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    // 字符相同，不需要操作
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    // 三种操作：插入、删除、替换
                    dp[i][j] = Math.min(
                        Math.min(dp[i][j - 1] + 1,    // 插入
                                dp[i - 1][j] + 1),    // 删除
                        dp[i - 1][j - 1] + 1          // 替换
                    );
                }
            }
        }
        
        return dp[m][n];
    }
    
    public static void main(String[] args) {
        EditDistance ed = new EditDistance();
        String word1 = "horse", word2 = "ros";
        System.out.println("编辑距离: " + ed.minDistance(word1, word2));
    }
}