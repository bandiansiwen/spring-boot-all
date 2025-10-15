package com.imp.all.framework.redis.core;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Longlin
 * @date 2023/1/4 10:41
 * @description
 */
public class ImpRedisKeyRegistry {
    /**
     * Redis RedisKeyDefine 数组
     */
    private static final List<ImpRedisKeyDefine> DEFINES = new ArrayList<>();

    public static void add(ImpRedisKeyDefine define) {
        DEFINES.add(define);
    }

    public static List<ImpRedisKeyDefine> list() {
        return DEFINES;
    }

    public static int size() {
        return DEFINES.size();
    }
}
