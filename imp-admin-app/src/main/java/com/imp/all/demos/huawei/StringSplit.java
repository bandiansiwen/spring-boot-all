package com.imp.all.demos.huawei;

import java.util.Locale;
import java.util.Scanner;

/**
 * @author Longlin
 * @date 2023/2/6 22:41
 * @description 字符串分割
 * 给定一个非空字符串S，其被N个‘-’分隔成N+1的子串，给定正整数K，要求除第一个子串外，其余的子串每K个字符组成新的子串，并用‘-’分隔。
 * 对于新组成的每一个子串，如果它含有的小写字母比大写字母多，则将这个子串的所有大写字母转换为小写字母；反之，如果它含有的大写字母比小写字母多，则将这个子串的所有小写字母转换为大写字母；大小写字母的数量相等时，不做转换。
 * 输入描述:
 * 输入为两行，第一行为参数K，第二行为字符串S。
 * 输出描述:
 * 输出转换后的字符串。
 */
public class StringSplit {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            int k = Integer.parseInt(sc.nextLine());
            String[] strings = sc.nextLine().split("-");
            String s = "";
            for(int i = 1; i < strings.length; i++) {
                s = s + strings[i];
            }
            int index = 0;
            StringBuffer buffer = new StringBuffer();
            buffer.append(strings[0]);
            // 每K个字符组成新的子串
            while (s.length() - index > k) {
                buffer.append("-" + caseConversion(s.substring(index, index + k)));
                index += k;
            }
            // 剩余的为一个字符串
            if (s.length() - index > 0) {
                buffer.append("-" + caseConversion(s.substring(index)));
            }
            System.out.println(buffer);
        }
    }

    private static String caseConversion(String s) {
        int upperCaseLength = s.length() - s.replaceAll("[A-Z]", "").length();
        int lowerCaseLength = s.length() - s.replaceAll("[a-z]", "").length();
        if (upperCaseLength > lowerCaseLength) {
            return s.toUpperCase(Locale.ROOT);
        }
        if (lowerCaseLength > upperCaseLength) {
            return s.toLowerCase(Locale.ROOT);
        }
        return s;
    }
}
