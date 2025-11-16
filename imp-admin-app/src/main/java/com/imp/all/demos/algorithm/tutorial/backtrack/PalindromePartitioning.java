package com.imp.all.demos.algorithm.tutorial.backtrack;

import java.util.*;

/**
 * @Author Daniel
 * @Date 2025/11/16/周日 18:47
 * @Description 分割回文串
 **/
public class PalindromePartitioning {
    public List<List<String>> partition(String s) {
        List<List<String>> result = new ArrayList<>();
        backtrack(s, 0, new ArrayList<>(), result);
        return result;
    }
    
    private void backtrack(String s, int start, List<String> path, List<List<String>> result) {
        // 终止条件：已经处理完整个字符串
        if (start == s.length()) {
            result.add(new ArrayList<>(path));
            return;
        }
        
        for (int i = start; i < s.length(); i++) {
            // 检查当前子串是否是回文
            if (isPalindrome(s, start, i)) {
                // 做选择：将回文子串加入路径
                path.add(s.substring(start, i + 1));
                
                // 递归处理剩余部分
                backtrack(s, i + 1, path, result);
                
                // 撤销选择
                path.remove(path.size() - 1);
            }
        }
    }
    
    private boolean isPalindrome(String s, int left, int right) {
        while (left < right) {
            if (s.charAt(left) != s.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }
    
    public static void main(String[] args) {
        PalindromePartitioning solution = new PalindromePartitioning();
        String s = "aab";
        List<List<String>> result = solution.partition(s);
        System.out.println(result);
        // 输出：[[a, a, b], [aa, b]]
    }
}