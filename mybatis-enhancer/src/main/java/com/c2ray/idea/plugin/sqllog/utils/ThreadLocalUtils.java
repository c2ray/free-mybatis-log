package com.c2ray.idea.plugin.sqllog.utils;

/**
 * @author c2ray
 * @since 2023/7/26
 */
public class ThreadLocalUtils {

    private static final ThreadLocal<String> MYBATIS_SQL_ID = new ThreadLocal<>();

    private static final ThreadLocal<String> MYBATIS_PLUS_SQL_ID = new ThreadLocal<>();

    public static void setMybatisSqlId(String mybatisSqlId) {
        MYBATIS_SQL_ID.set(mybatisSqlId);
    }

    public static String getMybatisSqlId() {
        return MYBATIS_SQL_ID.get();
    }


    public static void setMybatisPlusSqlId(String mybatisSqlId) {
        MYBATIS_PLUS_SQL_ID.set(mybatisSqlId);
    }

    public static String getMybatisPlusSqlId() {
        return MYBATIS_PLUS_SQL_ID.get();
    }


}
