package com.c2ray.idea.plugin.sqllog.service;

import com.c2ray.idea.plugin.sqllog.protocol.SqlLogProtocol;
import com.intellij.openapi.project.Project;

/**
 * @author c2ray
 * @since 2023/5/18
 */
public interface SqlLogService {
    void init();

    void register(int process, Project project);

    void printProtocol(SqlLogProtocol sqlLogProtocol);
}
