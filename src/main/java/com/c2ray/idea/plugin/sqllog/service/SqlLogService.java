package com.c2ray.idea.plugin.sqllog.service;

import com.c2ray.idea.plugin.sqllog.protocol.SqlLogProtocol;
import com.intellij.openapi.project.Project;

/**
 * @author c2ray
 * @since 2023/5/18
 */
public interface SqlLogService {
    void init();

    void register(int pid, Project project, com.sun.tools.attach.VirtualMachine vm);

    void detach(int pid);

    boolean isAttaching(int pid);

    void dealProtocol(SqlLogProtocol sqlLogProtocol);
}
