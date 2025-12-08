package com.small.rose.demo.config;

/**
 * @Project: demo-boot3-g
 * @Author: 张小菜
 * @Description: [ DataSourceContextHolder ] 说明： 无
 * @Function: 功能描述： 数据源上下文持有者
 * @Date: 2025/12/8 周一 21:36
 * @Version: v1.0
 */
public class DataSourceContextHolder {

    // 使用ThreadLocal来存储数据源类型，确保线程安全
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    /**
     * 设置当前线程的数据源类型
     * @param dataSourceType 数据源类型标识
     */
    public static void setDataSourceType(String dataSourceType) {
        contextHolder.set(dataSourceType);
    }

    /**
     * 获取当前线程的数据源类型
     * @return 当前线程使用的数据源类型
     */
    public static String getDataSourceType() {
        return contextHolder.get();
    }

    /**
     * 清除当前线程的数据源类型
     * 用于防止内存泄漏，特别是在线程池等场景下
     */
    public static void clearDataSourceType() {
        contextHolder.remove();
    }
}
