package com.c2ray.idea.plugin.sqllog.protocol;

/**
 * @author c2ray
 * @since 2023/5/18
 */
public class SqlLogProtocol {

    private final String frameworkName;

    private final Integer pid;

    private final String sql;

    /**
     * assemble protocol
     *
     * @param frameworkName
     * @param pid
     * @param sql
     */
    public SqlLogProtocol(String frameworkName, Integer pid, String sql) {
        this.frameworkName = frameworkName;
        this.pid = pid;
        this.sql = sql;
    }

    /**
     * parse protocol to SqlLogProtocol
     */
    public SqlLogProtocol(String str) {
        String[] splits = str.split(" ", 1);
        String[] props = splits[0].split(":");
        this.frameworkName = props[0];
        this.pid = Integer.valueOf(props[1]);
        this.sql = splits[1];
    }

    /**
     * e.g: mybatis:1119 select * from user
     *
     * @return
     */
    public String getProtocol() {
        return String.format("%s:%s %s", frameworkName, pid, sql);
    }


    @Override
    public String toString() {
        return getProtocol();
    }
}
