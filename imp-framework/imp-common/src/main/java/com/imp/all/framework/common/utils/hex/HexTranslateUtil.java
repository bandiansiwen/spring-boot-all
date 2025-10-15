package com.imp.all.framework.common.utils.hex;

import java.util.Stack;

/**
 * @author Longlin
 * @date 2023/2/3 16:37
 * @description 可用于短链系统设计
 */
public class HexTranslateUtil {

    private static final  char[] charSet = "qwertyuiopasdfghjklzxcvbnm0123456789QWERTYUIOPASDFGHJKLZXCVBNM".toCharArray();

    /**
     * 10进制转62进制
     * @param number
     * @return
     */
    public static String hex10To62(Long number) {
        Long rest = number;
        Stack<Character> stack = new Stack<Character>();
        StringBuilder result = new StringBuilder();
        while (rest!=0) {
            stack.add(charSet[new Long(rest-(rest/62)*62).intValue()]);
            rest=rest/62;
        }
        while (!stack.isEmpty()) {
            result.append(stack.pop());
        }
        return result.toString();
    }

    /**
     * 62进制转10进制
     * @param sixtyStr
     * @return
     */
    public static String hex62To10(String sixtyStr){
        long dst = 0L;
        for(int i=0; i<sixtyStr.length(); i++) {
            char c = sixtyStr.charAt(i);
            for(int j=0; j<charSet.length; j++) {
                if(c == charSet[j]) {
                    dst = (dst * 62) + j;
                    break;
                }
            }
        }
        return Long.toString(dst);
    }

    public static void main(String[] args) {
        Long number = 123456789L;
        String s = hex10To62(number);
        String s1 = hex62To10(s);
        System.out.println(s);
        System.out.println(s1);
    }
}
