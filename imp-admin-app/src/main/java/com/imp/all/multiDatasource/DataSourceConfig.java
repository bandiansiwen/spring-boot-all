package com.imp.all.multiDatasource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Longlin
 * @date 2021/9/10 15:33
 * @description 开启即为多数据源，读写分离
 */
//@Configuration
public class DataSourceConfig {

    /**
     * mybatis 配置
     */
    @Resource(name = "dynamicDatasource")
    private DataSourceRouter dynamicDatasource;

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dynamicDatasource);
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml"));
        return factoryBean.getObject();
    }

    @Bean
    public PlatformTransactionManager platformTransactionManager() {
        return new DataSourceTransactionManager(dynamicDatasource);
    }

    /**
     * 数据源配置
     * @return
     */
    @Bean(name = "masterDataSource")
    @ConfigurationProperties(prefix = "custom.datasource.master")
    public DataSource masterDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "slave1DataSource")
    @ConfigurationProperties(prefix = "custom.datasource.slave1")
    public DataSource slave1DataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "slave2DataSource")
    @ConfigurationProperties(prefix = "custom.datasource.slave2")
    public DataSource slave2DataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "dynamicDatasource")
    @DependsOn({ "masterDataSource", "slave1DataSource", "slave2DataSource"})
    public DataSourceRouter dynamicDatasource(@Qualifier("masterDataSource") DataSource masterDataSource,
                                              @Qualifier("slave1DataSource") DataSource slave1DataSource,
                                              @Qualifier("slave2DataSource") DataSource slave2DataSource) {
        DataSourceRouter dataSourceRouter = new DataSourceRouter();
        dataSourceRouter.setDefaultTargetDataSource(masterDataSource);

        Map<Object, Object> targetDataSource = new HashMap<>();
        targetDataSource.put(DBTypeEnum.MASTER, masterDataSource);
        targetDataSource.put(DBTypeEnum.SLAVE1, slave1DataSource);
        targetDataSource.put(DBTypeEnum.SLAVE2, slave2DataSource);

        dataSourceRouter.setTargetDataSources(targetDataSource);
        return dataSourceRouter;
    }

    /**
     * aop 配置
     * @return
     */
    @Bean
    public DataSourceRouteAspect dataSourceRouteAspect() {
        return new DataSourceRouteAspect();
    }

    @Bean
    public ReadWriteSeparateAspect readWriteSeparateAspect() {
        return new ReadWriteSeparateAspect();
    }
}
