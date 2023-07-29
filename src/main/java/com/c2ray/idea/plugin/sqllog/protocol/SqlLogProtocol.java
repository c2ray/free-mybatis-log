package com.c2ray.idea.plugin.sqllog.protocol;

/**
 * @author c2ray
 * @since 2023/5/18
 */
public class SqlLogProtocol {

    private final String frameworkName;

    private final Integer pid;

    private final String sql;

    private final String methodName;

    private final Integer status;


    public SqlLogProtocol(String frameworkName, Integer pid, Integer status, String methodName, String sql) {
        this.frameworkName = frameworkName;
        this.pid = pid;
        this.status = status;
        this.methodName = methodName;
        this.sql = sql;
    }

    public String getFrameworkName() {
        return frameworkName;
    }

    public Integer getPid() {
        return pid;
    }

    public String getSql() {
        return sql;
    }

    public String getMethodName() {
        return methodName;
    }

    public Integer getStatus() {
        return status;
    }
}
