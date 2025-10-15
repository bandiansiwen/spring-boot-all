package com.imp.all.file.utils;

public class IMCoreTextUtils {
    public static boolean isEmpty(String str) {
        if (str == null || str.length() == 0) {
            return true;
        }
        return false;
    }

    public static boolean equals(String text1, String text2) {
        if (text1 == null && text2 == null) {
            return true;
        }
        if (text1 != null) {
            return text1.equals(text2);
        }
        return false;
    }

    public static boolean equals(String text1, String text2, boolean matchCase) {
        if (matchCase) {
            return equals(text1, text2);
        }
        if (text1 == null && text2 == null) {
            return true;
        }
        if (text1 != null && text2 != null) {
            return text1.toUpperCase().equals(text2.toUpperCase());
        }
        return false;
    }
}
