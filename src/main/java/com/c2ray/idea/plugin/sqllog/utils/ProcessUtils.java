package com.c2ray.idea.plugin.sqllog.utils;

import com.intellij.execution.process.ProcessHandler;

/**
 * @author c2ray
 * @since 2023/5/17
 */
public class ProcessUtils {

    public static boolean isRunning(ProcessHandler processHandler) {
        return processHandler != null
                && processHandler.isStartNotified()
                && !processHandler.isProcessTerminated()
                && !processHandler.isProcessTerminating();
    }

    public static boolean isJavaProcess(ProcessHandler processHandler) {
        String command = processHandler.toString();
        String[] cmdArr = command.split(" ");
        String javaCommand = cmdArr[0];
        String osName = System.getProperty("os.name");

        if (osName.toLowerCase().startsWith("win")) {
            return javaCommand.endsWith("java.exe");
        } else {
            return javaCommand.endsWith("java");
        }
    }


}
