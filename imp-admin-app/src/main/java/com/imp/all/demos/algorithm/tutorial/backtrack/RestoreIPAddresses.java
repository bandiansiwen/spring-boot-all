package com.imp.all.demos.algorithm.tutorial.backtrack;

import java.util.*;

/**
 * @Author Daniel
 * @Date 2025/11/16/周日 18:47
 * @Description 复原IP地址
 **/
public class RestoreIPAddresses {
    public List<String> restoreIpAddresses(String s) {
        List<String> result = new ArrayList<>();
        if (s.length() < 4 || s.length() > 12) {
            return result;
        }
        backtrack(s, 0, new ArrayList<>(), result);
        return result;
    }
    
    private void backtrack(String s, int start, List<String> path, List<String> result) {
        // 终止条件：已经分成4段并且用完了所有字符
        if (path.size() == 4) {
            if (start == s.length()) {
                result.add(String.join(".", path));
            }
            return;
        }
        
        // 尝试1-3位数字
        for (int i = 1; i <= 3; i++) {
            if (start + i > s.length()) {
                break;
            }
            
            String segment = s.substring(start, start + i);
            
            // 检查段是否有效
            if (isValidSegment(segment)) {
                // 做选择
                path.add(segment);
                
                // 递归
                backtrack(s, start + i, path, result);
                
                // 撤销选择
                path.remove(path.size() - 1);
            }
        }
    }
    
    private boolean isValidSegment(String segment) {
        // 长度大于1时不能以0开头
        if (segment.length() > 1 && segment.charAt(0) == '0') {
            return false;
        }
        
        // 必须在0-255范围内
        int num = Integer.parseInt(segment);
        return num >= 0 && num <= 255;
    }
    
    public static void main(String[] args) {
        RestoreIPAddresses solution = new RestoreIPAddresses();
        String s = "25525511135";
        List<String> result = solution.restoreIpAddresses(s);
        System.out.println(result);
        // 输出：[255.255.11.135, 255.255.111.35]
    }
}