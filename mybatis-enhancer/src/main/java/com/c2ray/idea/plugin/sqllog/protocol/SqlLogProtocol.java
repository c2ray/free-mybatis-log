package com.c2ray.idea.plugin.sqllog.protocol;

/**
 * @author c2ray
 * @since 2023/5/18
 */
public class SqlLogProtocol {

    private final String frameworkName;

    private final Integer pid;

    private final Integer status;

    private final String methodName;

    private final String sql;

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

    public Integer getStatus() {
        return status;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getSql() {
        return sql;
    }
}
