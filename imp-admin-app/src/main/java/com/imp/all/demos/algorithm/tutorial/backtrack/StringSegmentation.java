package com.imp.all.demos.algorithm.tutorial.backtrack;

import java.util.*;

/**
 * @Author Daniel
 * @Date 2025/11/16/周日 18:47
 * @Description 字符串的不同分割方式
 **/
public class StringSegmentation {
    public List<List<String>> segmentString(String s, Set<String> dictionary) {
        List<List<String>> result = new ArrayList<>();
        backtrack(s, 0, new ArrayList<>(), result, dictionary);
        return result;
    }
    
    private void backtrack(String s, int start, List<String> path, 
                          List<List<String>> result, Set<String> dictionary) {
        if (start == s.length()) {
            result.add(new ArrayList<>(path));
            return;
        }
        
        for (int i = start; i < s.length(); i++) {
            String word = s.substring(start, i + 1);
            
            // 如果当前子串在字典中
            if (dictionary.contains(word)) {
                // 做选择
                path.add(word);
                
                // 递归处理剩余部分
                backtrack(s, i + 1, path, result, dictionary);
                
                // 撤销选择
                path.remove(path.size() - 1);
            }
        }
    }
    
    public static void main(String[] args) {
        StringSegmentation solution = new StringSegmentation();
        String s = "catsanddog";
        Set<String> dictionary = new HashSet<>(Arrays.asList(
            "cat", "cats", "and", "sand", "dog"
        ));
        
        List<List<String>> result = solution.segmentString(s, dictionary);
        System.out.println(result);
        // 输出：[[cat, sand, dog], [cats, and, dog]]
    }
}