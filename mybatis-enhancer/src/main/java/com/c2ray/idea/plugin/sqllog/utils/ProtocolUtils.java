package com.c2ray.idea.plugin.sqllog.utils;

import com.c2ray.idea.plugin.sqllog.config.ProjectConfig;
import com.c2ray.idea.plugin.sqllog.protocol.SqlLogProtocol;

/**
 * @author c2ray
 * @since 2023/5/18
 */
public class ProtocolUtils {


    public static SqlLogProtocol getProtocol(String framework, Integer status) {
        return getProtocol(framework, status, null, null);
    }

    public static SqlLogProtocol getProtocol(String framework, Integer status, String methodName, String sql) {
        return new SqlLogProtocol(framework, ProjectConfig.getPid(), status, methodName, sql);
    }

}
