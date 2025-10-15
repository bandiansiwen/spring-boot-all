package com.imp.all.framework.web.openapi.sign;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author Longlin
 * @date 2023/1/16 14:31
 * @description
 */
@Slf4j
public class SignValidator {

    /**
     * 校验签名
     * @param signParamMap 签名的对象，字段使用 @SignParam
     * @param secret 秘钥
     * @param sign 签名
     * @return
     */
    public static boolean validate(Map<String,Object> signParamMap, String secret, String signMethod, String sign){
        if(signParamMap == null || signParamMap.size() == 0){
            return false;
        }

        //默认md5
        if(!StringUtils.hasText(signMethod)){
            signMethod = "md5";
        }

        //获取签名
        String expectSign = getSign(signMethod, signParamMap, secret);

        return expectSign.equalsIgnoreCase(sign);
    }

    /**
     * 包含body、params 校验签名
     * @param body
     * @param signParamMap
     * @param secret
     * @param signMethod
     * @param sign
     * @return
     */
    public static boolean validate(Object body, Map<String,Object> signParamMap, String secret, String signMethod, String sign){
        if (body==null && CollectionUtils.isEmpty(signParamMap)) {
            return false;

        }

        //默认md5
        if(!StringUtils.hasText(signMethod)){
            signMethod = "md5";
        }

        //获取签名
        String expectSign = getSign(signMethod, body,signParamMap,secret);

        return expectSign.equalsIgnoreCase(sign);
    }

    /**
     * 是否支持改签名算法
     * @param signMethod
     * @return
     */
    public static boolean checkSupportSignMethod(String signMethod){
        if(signMethod.equalsIgnoreCase("md5")){
            return true;
        }

        return false;
    }

    /**
     * 计算签名
     * @param map
     * @param secret
     * @return
     */
    public static String getSign(String signMethod, Map<String,Object> map, String secret){
        if(!checkSupportSignMethod(signMethod)){
            throw new RuntimeException(MessageFormat.format("不支持签名算法{0}", signMethod));
        }

        // 第一步：检查参数是否已经排序
        String[] keys = map.keySet().toArray(new String[0]);
        Arrays.sort(keys);

        // 第二步：把所有参数名和参数值串在一起
        StringBuilder query = new StringBuilder();

        query.append(secret);
        for (String key : keys) {
            String value = String.valueOf(map.get(key));
            query.append(key).append(value);
        }
        query.append(secret);

        // 第三步：计算签名
        String sign = DigestUtils.md5DigestAsHex(query.toString().getBytes());

        if(log.isDebugEnabled()){
            log.debug("\n--->signStr : "+query.toString()+"\n--->sign : "+sign);
        }

        return sign;
    }

    /**
     * 包含body、params 计算签名
     * @param signMethod
     * @param body
     * @param params
     * @param secret
     * @return
     */
    public static String getSign(String signMethod, Object body, Map<String, Object> params, String secret) {
        if(!checkSupportSignMethod(signMethod)){
            throw new RuntimeException(MessageFormat.format("不支持签名算法{0}", signMethod));
        }

        StringBuilder sb = new StringBuilder();
        // md5加密 参数前加上secret
        sb.append(secret);
        if (!CollectionUtils.isEmpty(params)) {

            // params参数是排序
            String[] keys = params.keySet().toArray(new String[0]);
            Arrays.sort(keys);
            // 把所有参数名和参数值串在一起
            for (String key : keys) {
                String value = String.valueOf(params.get(key));
                sb.append(key).append(value);
            }
        }
        // 把body参数串在一起
        if (body !=null) {
            sb.append('#');
            appendBody(body, sb);

        }
        // md5加密 参数后加上secret
        sb.append(secret);

        // 第三步：计算签名
        String sign = DigestUtils.md5DigestAsHex(sb.toString().getBytes());

        if(log.isDebugEnabled()){
            log.debug("\n--->signStr : "+sb.toString()+"\n--->sign : "+sign);
        }

        return sign;
    }

    public static void appendBody(Object body, StringBuilder sb) {
        String jsonTxt;
        if(body == null){
            return;
        }
        if(body instanceof String){
            jsonTxt = (String) body;
            if(!JSONUtil.isTypeJSON(jsonTxt)){
                sb.append(jsonTxt);
                return;
            }
        }else{
            jsonTxt = JSONUtil.toJsonStr(body);
        }
        sb.append(JSONUtil.toJsonStr(deepSort(JSONUtil.parse(jsonTxt))));
    }

    private static Object deepSort(Object data){
        if(data instanceof JSONObject) {
            return deepSortObject((JSONObject) data);
        }else if(data instanceof JSONArray){
            deepSortArray((JSONArray) data);
        }
        return data;
    }

    private static Object deepSortObject(JSONObject data) {
        for (Map.Entry<String, Object> e : ((Map<String, Object>) data).entrySet()) {
            Object value = e.getValue();
            if (value instanceof JSONObject) {
                e.setValue(deepSortObject((JSONObject) value));
            } else if (value instanceof JSONArray) {
                deepSortArray((JSONArray) value);
            }else{
                log.warn("deepSortArray unknown json type:{}", value == null ? "null" : value.getClass().getName());
            }
        }
        return new TreeMap<>(data);
    }

    private static void deepSortArray(JSONArray data) {
        for (int i = 0, arraySize = data.size(); i < arraySize; i++) {
            Object obj = data.get(i);
            if (obj instanceof JSONObject) {
                data.set(i, deepSortObject((JSONObject) obj));
            }else if(obj instanceof JSONArray){
                deepSortArray((JSONArray) obj);
            }else{
                log.warn("deepSortArray unknown json type:{}", obj == null ? "null" : obj.getClass().getName());
            }
        }
    }
    /**
     *
     * @param data
     * @param sortedVal 防止循环嵌套数据
     * @return
     */
    private static SortedMap<String,Object> deepSortMap(Map<String,Object> data, Map<Map<String,Object>,SortedMap<String,Object>> sortedVal){
        if(sortedVal!=null) {
            SortedMap<String,Object> sortedMap = sortedVal.get(data);
            if(sortedMap!=null){
                if(log.isWarnEnabled()){
                    log.warn("存在循环嵌套数据！！，data:{}", JSONUtil.toJsonStr(data));
                }
                return sortedMap;
            }
        }
        if(sortedVal==null){
            sortedVal = new TreeMap<>((o1, o2) -> o1==o2?0:1);
        }
        TreeMap<String, Object> res = new TreeMap<>(data);
        sortedVal.put(data,res);
        for(Map.Entry<String,Object> e: data.entrySet()){
            if(e.getValue() instanceof Map){
                e.setValue(deepSortMap((Map) e.getValue(),sortedVal));
            }
        }
        return res;
    }

    /**
     * 计算签名
     * @param map
     * @param secret
     * @return
     */
    public static String getSign(Map<String,Object> map, String secret){
        //默认使用 md5 计算签名
        return getSign("md5", map, secret);
    }

}
