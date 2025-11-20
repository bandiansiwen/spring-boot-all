package com.imp.all.demos.algorithm.array;

import java.util.HashMap;

/**
 * @author Longlin
 * @date 2023/4/18 13:59
 * @description
 */
public class LeftRightArrayDemos {

    /**
     *  二分查找
     */
    public static int binarySearch(int[] nums, int target) {
        // 一左一右两个指针相向而行
        int left = 0, right = nums.length - 1;
        while(left <= right) {
            int mid = (right + left) / 2;
            if(nums[mid] == target)
                return mid;
            else if (nums[mid] < target)
                left = mid + 1;
            else if (nums[mid] > target)
                right = mid - 1;
        }
        return -1;
    }

    /**
     * 力扣第 167 题「 两数之和 II」
     * <a href="https://leetcode.cn/problems/two-sum-ii-input-array-is-sorted/">...</a>
     */
    public static int[] twoSum(int[] sums, int target) {
        int left = 0, right = sums.length-1;
        while (left < right) {
            int val = sums[left] + sums[right];
            if (val == target) {
                // 题目要求的索引是从 1 开始的
                return new int[]{left+1, right+1};
            }
            else if(val < target) {
                left = left + 1;
            }
            else if (val > target) {
                right = right - 1;
            }
        }
        return new int[]{-1, -1};
    }

    /**
     * 力扣第 344 题「 反转字符串」
     * <a href="https://leetcode.cn/problems/reverse-string/">...</a>
     */
    public static void reverseString(char[] s){
        // 一左一右两个指针相向而行
        int left = 0, right = s.length - 1;
        while (left < right) {
            // 交换 s[left] 和 s[right]
            char temp = s[right];
            s[right] = s[left];
            s[left] = temp;
            left++;
            right--;
        }
    }

    /**
     * 回文串判断
     * 回文串就是正着读和反着读都一样的字符串。
     * 比如说字符串 aba 和 abba 都是回文串，因为它们对称，反过来还是和本身一样；反之，字符串 abac 就不是回文串。
     */
    public static boolean isPalindrome(String s) {
        // 一左一右两个指针相向而行
        int left = 0, right = s.length() - 1;
        while (left < right) {
            if (s.charAt(left) != s.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }

    /**
     * 力扣第 5 题「 最长回文子串」
     * <a href="https://leetcode.cn/problems/longest-palindromic-substring/">...</a>
     */
    public static String longestPalindrome(String s) {
        String res = "";
        for (int i = 0; i < s.length(); i++) {
            // 以 s[i] 为中心的最长回文子串
            String s1 = palindrome(s, i, i);
            // 以 s[i] 和 s[i+1] 为中心的最长回文子串
            String s2 = palindrome(s, i, i + 1);
            // res = longest(res, s1, s2)
            res = res.length() > s1.length() ? res : s1;
            res = res.length() > s2.length() ? res : s2;
        }
        return res;
    }

    // 在 s 中寻找以 s[l] 和 s[r] 为中心的最长回文串
    // 如果输入相同的 l 和 r，就相当于寻找长度为奇数的回文串，如果输入相邻的 l 和 r，则相当于寻找长度为偶数的回文串。
    public static String palindrome(String s, int l, int r) {
        // 防止索引越界
        while (l>=0 && r<s.length() && s.charAt(l) == s.charAt(r)) {
            // 双指针，两边展开
            l--;
            r++;
        }
        // 返回以 s[l] 和 s[r] 为中心的最长回文串
        return s.substring(l+1,r);
    }

    /**
     * 无重复最长子串
     */
    public static int lengthOfLongestSubstring(String s) {
        if(s.length()<=0) return 0;
        HashMap<Character, Integer> map = new HashMap<>();
        int maxLength = 1;
        map.put(s.charAt(0), 0);
        int i=0;
        for(int j=1;j<s.length();j++) {
            Integer i1 = map.get(s.charAt(j));
            if(i1 == null || i1<i) {
                maxLength = Math.max(maxLength, j-i+1);
            }
            else {
                i=map.get(s.charAt(j)) + 1;
            }
            map.put(s.charAt(j), j);
        }

        return maxLength;
    }

    public static boolean checkStr(String str) {

        boolean flag = false;

        if (str == null || str.isEmpty() || str.length()%2 != 0) {
            return false;
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("(", ")");
        map.put("[", "]");
        map.put("{", "}");
        int i = 0;
        int total = str.length() / 2;
        for(; i < total; i++) {
            char first = str.charAt(i);
            char last = str.charAt(str.length()-1-i);
            if (!map.get(first + "").equals(last + "")) {
                break;
            }
        }
        if (i == total) {
            flag = true;
        }
        return flag;
    }


    public static void main(String[] args) {

//        String s = "tmmzuxt";
//        int i = lengthOfLongestSubstring(s);
//        System.out.println(i);

        System.out.println(checkStr("[()]"));

        System.out.println(checkStr("[(])"));

//        int[] nums3 = new int[]{0,1,2,3,4,5};
//        int search = binarySearch(nums3, 5);
//        System.out.println(search);
//
//        int[] twoS = twoSum(nums3, 3);
//        System.out.println(Arrays.toString(twoS));
//
//        char[] s = new char[]{'a', 'b', 'c', 'd', 'e'};
//        reverseString(s);
//        System.out.println(s);
//
//        boolean res = isPalindrome("abba");
//        System.out.println(res);
//
//        String s1 = longestPalindrome("babad");
//        System.out.println(s1);
//        String s2 = longestPalindrome("cbbd");
//        System.out.println(s2);


    }
}
