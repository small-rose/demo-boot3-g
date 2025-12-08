package com.small.rose.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Project: demo-boot3-g
 * @Author: 张小菜
 * @Description: [ DatasourceConfig ] 说明： 无
 * @Function: 功能描述： 无
 * @Date: 2025/12/8 周一 21:34
 * @Version: v1.0
 */
@Configuration
public class DatasourceConfig {
    /**
     * 主数据源配置
     * 使用@Primary注解标记为默认数据源
     * 定义名为primaryDataSource的Bean
     */
    @Primary
    @Bean(name = "dataSource")
    // 从配置文件中读取spring.datasource.primary前缀的配置项
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        // 创建并返回主数据源
        return DataSourceBuilder.create().build();
    }

    /**
     * 主数据源配置
     * 使用@Primary注解标记为默认数据源
     * 定义名为primaryDataSource的Bean
     */
    @Primary
    @Bean(name = "primaryDataSource")
    // 从配置文件中读取spring.datasource.primary前缀的配置项
    @ConfigurationProperties(prefix = "app.datasource.primary")
    public DataSource primaryDataSource() {
        // 创建并返回主数据源
        return DataSourceBuilder.create().build();
    }

    /**
     * 定义名为secondaryDataSource的Bean
     * @return
     */
    @Bean(name = "secondaryDataSource")
    // 从配置文件中读取spring.datasource.secondary前缀的配置项
    @ConfigurationProperties(prefix = "app.datasource.secondary")
    public DataSource secondaryDataSource() {
        // 创建并返回从数据源
        return DataSourceBuilder.create().build();
    }

    /**
     * 动态数据源配置
     * 定义名为dynamicDataSource的Bean
     */
    @Bean(name = "dynamicDataSource")
    public DataSource dynamicDataSource() {
        // 创建动态数据源实例
        JpaConfig.DynamicDataSource dynamicDataSource = new JpaConfig.DynamicDataSource();
        // 创建数据源映射，用于存储多个数据源
        Map<Object, Object> dataSourceMap = new HashMap<>();
        // 将主数据源和从数据源添加到映射中
        dataSourceMap.put("primary", primaryDataSource());
        dataSourceMap.put("secondary", secondaryDataSource());
        // 设置目标数据源映射
        dynamicDataSource.setTargetDataSources(dataSourceMap);
        // 设置默认数据源为主数据源
        dynamicDataSource.setDefaultTargetDataSource(primaryDataSource());
        // 返回配置好的动态数据源
        return dynamicDataSource;
    }

}
