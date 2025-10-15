package com.imp.all;

/**
 * @Author Daniel
 * @Date 2025/9/23/周二 10:43
 * @Description
 * 请编写一个函数，用来将 x 进制的数字转换为 y 进制。
 * 提示：
 *         - 进制的取值范围为 2 ~ 36
 *         - 当进制大于 10 时，则使用 A-Z 来表示超出 10 的数字。比如，
 * A 表示 10，B 表示 11，……，Z 表示 35
 *         - 参数 number 对应的数值大小在 int 范围内
 * 示例1：
 * 输入：("1001", 2, 4)
 * 输出："21"
 * 示例2：
 * 输入：("AF0", 16, 17)
 * 输出："9BC"
 **/
public class Test {

    String convertBase(String number, int fromBase, int toBase) {
        // your code here
        // 处理特殊情况：输入为0
        if (number.equals("0")) {
            return "0";
        }

        // 第一步：将源进制转换为十进制
        int decimal = 0;
        for (int i = 0; i < number.length(); i++) {
            int digit = getDigit(number, fromBase, i);

            decimal = decimal * fromBase + digit;
        }

        // 第二步：将十进制转换为目标进制
        StringBuilder result = new StringBuilder();
        while (decimal > 0) {
            int remainder = decimal % toBase;
            // 将余数转换为对应的字符
            if (remainder < 10) {
                result.append((char) ('0' + remainder));
            } else {
                result.append((char) ('A' + remainder - 10));
            }
            decimal = decimal / toBase;
        }

        // 反转结果，因为我们是从低位到高位存储的
        return result.reverse().toString();
    }

    private static int getDigit(String number, int fromBase, int i) {
        char c = number.charAt(i);
        int digit;

        // 解析字符对应的数值
        if (c >= '0' && c <= '9') {
            digit = c - '0';
        } else if (c >= 'A' && c <= 'Z') {
            digit = 10 + (c - 'A');
        } else {
            throw new IllegalArgumentException("无效字符: " + c);
        }

        // 检查数字是否超过源进制
        if (digit >= fromBase) {
            throw new IllegalArgumentException("数字 " + digit + " 不适合 " + fromBase + " 进制");
        }
        return digit;
    }

    public static void main(String[] args) {
        // Case 1
        testRun("1001", 2, 4, "21");

        // Case 2
        testRun("AF0", 16, 17, "9BC");
    }

    private static void testRun(String number, int fromBase, int toBase, String expected) {
        String actual = (new Test()).convertBase(number, fromBase, toBase);
        System.out.println("Input: " + number + ", " + fromBase + ", " + toBase);
        System.out.println("Expected: " + expected);
        System.out.println("Actual  : " + actual);
        if (expected.equals(actual)) {
            System.out.println("Test passed.");
        } else {
            System.out.println("Test failed.");
        }
        System.out.println();
    }
}

