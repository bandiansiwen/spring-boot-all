package com.imp.all.multiDatasource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author Longlin
 * @date 2021/9/10 16:10
 * @description
 */
@Slf4j
public class DataSourceRouter extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        DBTypeEnum dbTypeEnum = DataSourceContextHolder.getDBType();
        if (dbTypeEnum == null) {
            log.debug("null client database, use default {}", DBTypeEnum.MASTER);
            dbTypeEnum = DBTypeEnum.MASTER;
        }
        log.trace("use {} as database", dbTypeEnum);
        DataSourceContextHolder.clear();
        return dbTypeEnum;
    }
}
