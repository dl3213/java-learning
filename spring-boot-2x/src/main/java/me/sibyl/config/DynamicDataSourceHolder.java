package me.sibyl.config;

import lombok.extern.slf4j.Slf4j;
/**
 * @author dyingleaf3213
 * @Classname DataSourceContextHolder
 * @Description TODO
 * @Create 2023/04/09 01:24
 */
@Slf4j
public class DynamicDataSourceHolder {

    private static ThreadLocal<String> contextHolder = new ThreadLocal<>();
    public static final String DB_MASTER = "master";
    public static final String DB_SLAVE = "slave";

    public static String getDbType() {
        String db = contextHolder.get();
        if (db == null) {
            db = DB_SLAVE;
        }
        return db;
    }

    public static void setDBType(String str) {
        log.info("当前设置数据源为" + str);
        contextHolder.set(str);
    }

    public static void clearDbType() {
        contextHolder.remove();
    }

}