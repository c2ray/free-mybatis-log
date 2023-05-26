package com.c2ray.idea.plugin.core.config;

import java.lang.management.ManagementFactory;

/**
 * @author c2ray
 * @since 2023/5/18
 */
public class ProjectConfig {

    private static String PROJECT_NAME;

    private static Integer TARGET_PORT;

    private static Integer PID;

    public static void init(String projectName, Integer port) {
        PROJECT_NAME = projectName;
        TARGET_PORT = port;
        PID = getCurrentPid();
    }

    private static int getCurrentPid() {
        String mxBeanName = ManagementFactory.getRuntimeMXBean().getName();
        int pid = Integer.parseInt(mxBeanName.split("@")[0]);
        return pid;
    }

    public static Integer getPid() {
        return PID;
    }

    public static String getProjectName() {
        return PROJECT_NAME;
    }

    public static Integer getTargetPort() {
        return TARGET_PORT;
    }
}
