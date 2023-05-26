package com.c2ray.idea.plugin.core.utils;

import com.c2ray.idea.plugin.core.config.ProjectConfig;
import com.c2ray.idea.plugin.core.protocol.SqlLogProtocol;

/**
 * @author c2ray
 * @since 2023/5/18
 */
public class ProtocolUtils {

    public static SqlLogProtocol getProtocol(String framework, String sql) {
        return new SqlLogProtocol(framework, ProjectConfig.getPid(), sql);
    }

}
