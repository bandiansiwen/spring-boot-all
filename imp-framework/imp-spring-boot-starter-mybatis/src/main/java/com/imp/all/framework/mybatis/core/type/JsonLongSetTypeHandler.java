package com.imp.all.framework.mybatis.core.type;

import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.imp.all.framework.common.utils.json.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Set;

/**
 * @author Longlin
 * @date 2022/12/21 14:19
 * @description
 */
public class JsonLongSetTypeHandler extends AbstractJsonTypeHandler<Object> {


    private static final TypeReference<Set<Long>> TYPE_REFERENCE = new TypeReference<Set<Long>>(){};

    @Override
    protected Object parse(String json) {
        return JsonUtils.parseObject(json, TYPE_REFERENCE);
    }

    @Override
    protected String toJson(Object obj) {
        return JsonUtils.toJsonString(obj);
    }

}
