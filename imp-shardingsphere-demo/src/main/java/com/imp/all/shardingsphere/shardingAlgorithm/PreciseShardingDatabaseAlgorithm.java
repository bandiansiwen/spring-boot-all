package com.imp.all.shardingsphere.shardingAlgorithm;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;

import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

/**
 * @author Longlin
 * @date 2023/2/10 14:40
 * @description sharding jdbc 精准 `分库` 策略
 */
@Slf4j
public class PreciseShardingDatabaseAlgorithm implements StandardShardingAlgorithm<Long> {

    // 主库别名
    private static final String DBM = "db0";
    // 数据库数量
    private static final int dataBaseSize = 2;

    /**
     * @description: 分库策略，按用户编号最后一位数字对数据库数量取模
     *
     * @param dbNames 所有库名
     * @param preciseShardingValue 精确分片值，包括（columnName，logicTableName，value）
     * @return 表名
     */
    @Override
    public String doSharding(Collection<String> dbNames, PreciseShardingValue<Long> preciseShardingValue) {

        // 若走主库，直接返回主库
        if (dbNames.size() == 1) {
            Iterator<String> iterator = dbNames.iterator();
            String dbName = iterator.next();
            if (DBM.equals(dbName)) {
                return DBM;
            }
        }

        // 按数据库数量取模
        Long value = preciseShardingValue.getValue();
        long mod = value % dataBaseSize;
        for (String dbName : dbNames) {
            // 分库的规则
            if (dbName.endsWith(String.valueOf(mod))) {
                return dbName;
            }
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, RangeShardingValue<Long> shardingValue) {
        return null;
    }

    @Override
    public Properties getProps() {
        return null;
    }

    @Override
    public void init(Properties properties) {
        log.info("PreciseShardingDatabaseAlgorithm init 配置 {}", properties);
    }
}
