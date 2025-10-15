package com.imp.all.framework.mybatis.core.enums;

import com.baomidou.mybatisplus.annotation.DbType;

/**
 * @author Longlin
 * @date 2022/12/20 16:28
 * @description
 */
public class SqlConstants {

    /**
     * 数据库的类型
     */
    public static DbType DB_TYPE;

    public static void init(DbType dbType) {
        DB_TYPE = dbType;
    }
}
