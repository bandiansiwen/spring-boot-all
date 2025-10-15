package com.imp.all.framework.common.utils.converter;

/**
 * @author Longlin
 * @date 2022/1/20 16:36
 * @description
 */
public class ImpTypeConverterUtils {

    public static String getStringValue(Object obj) {
        return obj == null ? "" : obj.toString();
    }
}
