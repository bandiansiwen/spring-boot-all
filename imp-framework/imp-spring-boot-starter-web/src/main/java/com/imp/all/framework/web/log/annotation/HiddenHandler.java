package com.imp.all.framework.web.log.annotation;

import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.StrUtil;
import com.imp.all.framework.web.holder.ApplicationContextHolder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Longlin
 * @date 2022/8/31 11:05
 * @description
 */
public class HiddenHandler {

    private static final String reg = "\\$\\{([^}]*)\\}";
    private static final String startReg = "${";
    private static final String endReg = "}";

    public String hiddenString(String source, HiddenField hiddenField) {

        String desensitizedStr;

        String startIndex = hiddenField.startIndex();
        String endIndex = hiddenField.endIndex();

        if (StrUtil.isNotEmpty(startIndex) || StrUtil.isNotEmpty(endIndex)) {
            int startValue = getValue(startIndex);
            int endValue;
            if (StrUtil.isEmpty(endIndex)) {
                endValue = source.length();
            }
            else {
                endValue = getValue(endIndex);
            }
            desensitizedStr = StrUtil.hide(source, startValue, endValue);
        }
        else {
            HiddenMode hiddenMode = hiddenField.type();
            if (hiddenMode == HiddenMode.PHONE) {
                desensitizedStr = DesensitizedUtil.mobilePhone(source);
            }
            else if (hiddenMode == HiddenMode.EMAIL) {
                desensitizedStr = DesensitizedUtil.email(source);
            }
            else if (hiddenMode == HiddenMode.ID_CARD) {
                desensitizedStr = DesensitizedUtil.idCardNum(source, 4, 4);
            }
            else if (hiddenMode == HiddenMode.BANK_CARD) {
                desensitizedStr = DesensitizedUtil.bankCard(source);
            }
            else {
                desensitizedStr = DesensitizedUtil.password(source);
            }
        }
        return desensitizedStr;
    }

    public int getValue(String sourceValue) {
        int value;
        String valueStr;
        if (sourceValue.startsWith(startReg) && sourceValue.endsWith(endReg)) {
            String property = getContentInfo(sourceValue);
            valueStr = ApplicationContextHolder.getString(property);
        }
        else {
            valueStr = sourceValue;
        }
        if (StrUtil.isEmpty(valueStr)) {
            value = 0;
        }
        else {
            value = Integer.parseInt(valueStr);
        }
        return value;
    }

    public String getContentInfo(String content) {
        Pattern regex = Pattern.compile(reg);
        Matcher matcher = regex.matcher(content);
        String value = null;
        if(matcher.find()) {
            value = matcher.group(1);
        }
        return value;
    }
}
