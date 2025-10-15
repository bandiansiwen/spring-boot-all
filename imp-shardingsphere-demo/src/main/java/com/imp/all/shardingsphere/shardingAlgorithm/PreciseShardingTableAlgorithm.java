package com.imp.all.shardingsphere.shardingAlgorithm;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;

import java.util.Collection;
import java.util.Properties;

/**
 * @author Longlin
 * @date 2023/2/10 14:46
 * @description
 */
@Slf4j
public class PreciseShardingTableAlgorithm implements StandardShardingAlgorithm<Long> {

    // 分表数量
    private static final int tableSize = 10;

    /**
     * @description: 分表策略
     *
     * @param tableNames 所有表名
     * @param preciseShardingValue 精确分片值，包括（columnName，logicTableName，value）
     * @return 表名
     */
    @Override
    public String doSharding(Collection<String> tableNames, PreciseShardingValue<Long> preciseShardingValue) {

        // 按表数量取模
        // 截取用户编号倒数二三位数字，（如1234的倒数二三位为23）
        Long value = preciseShardingValue.getValue();
        long mod = value % tableSize;
        for (String tableName : tableNames) {
            // 分表的规则
            if (tableName.endsWith(String.valueOf(mod))) {
                return tableName;
            }
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<String> doSharding(Collection<String> collection, RangeShardingValue<Long> rangeShardingValue) {
        return null;
    }

    @Override
    public Properties getProps() {
        return null;
    }

    @Override
    public void init(Properties properties) {
        log.info("PreciseShardingTableAlgorithm init 配置 {}", properties);
    }
}
