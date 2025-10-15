package com.imp.all.multiDatasource;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Longlin
 * @date 2021/9/10 16:47
 * @description
 */
@Slf4j
public class DataSourceContextHolder {

    private static final ThreadLocal<DBTypeEnum> contextHolder = new ThreadLocal<>();
    private static final ThreadLocal<SourceType> sourceTypeHolder = new ThreadLocal<>();

    private static final AtomicInteger counter = new AtomicInteger(-1);

    public static void set(DBTypeEnum dbType, SourceType type) {
        SourceType sourceType = getSourceType();
        if (sourceType != null && sourceType.compareTo(type) > 0) {
            log.info("数据源无法修改！old={}, new={}", sourceType, type);
            return;
        }
        contextHolder.set(dbType);
        sourceTypeHolder.set(type);
    }

    public static void reSet(DBTypeEnum dbType) {
        contextHolder.set(dbType);
        sourceTypeHolder.set(SourceType.SYSTEM);
    }

    public static void systemSet(DBTypeEnum dbType) {
        set(dbType, SourceType.SYSTEM);
    }

    public static void annotationSet(DBTypeEnum dbType) {
        set(dbType, SourceType.ANNOTATION);
    }

    public static void customSet(DBTypeEnum dbType) {
        set(dbType, SourceType.CUSTOM);
    }

    public static DBTypeEnum getDBType() {
        return contextHolder.get();
    }

    public static SourceType getSourceType() {
        return sourceTypeHolder.get();
    }

    public static void clear() {
        contextHolder.remove();
        sourceTypeHolder.remove();
    }

    public static void master() {
        log.info("切换到master");
        systemSet(DBTypeEnum.MASTER);
    }

    public static void slave() {
        //  轮询
        int index = counter.getAndIncrement() % 2;
        if (counter.get() > 99) {
            counter.set(-1);
        }
        if (index == 0) {
            log.info("切换到slave1");
            systemSet(DBTypeEnum.SLAVE1);
        }else {
            log.info("切换到slave2");
            systemSet(DBTypeEnum.SLAVE2);
        }
    }
}
