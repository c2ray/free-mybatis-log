package com.c2ray.idea.plugin.sqllog.utils;

import org.apache.commons.lang.SystemUtils;

/**
 * @author c2ray
 * @since 2023/5/24
 */
public class PathUtils {

    /**
     * <pre>
     * 临时文件路径
     * windows: C:\Users\%USER_NAME%\AppData\Local\Temp
     * linux: /tmp
     * </pre>
     */
    private static final String TMP_PATH = SystemUtils.getJavaIoTmpDir().getPath();


    public static String getTmpPath() {
        return TMP_PATH;
    }

}
