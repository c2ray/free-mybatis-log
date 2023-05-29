package com.c2ray.idea.plugin.core.utils;

import com.c2ray.idea.plugin.core.config.ProjectConfig;
import com.c2ray.idea.plugin.core.printer.MybatisMessager;

import java.io.IOException;

/**
 * @author c2ray
 * @since 2023/5/15
 */
public class MessageUtils {

    private static MybatisMessager MYBATIS_PRINTER;

    public static void init() {
        MYBATIS_PRINTER = new MybatisMessager(ProjectConfig.getTargetPort());
    }

    public static void sendMybatisProtocol(String protocolStr) {
        try {
            MYBATIS_PRINTER.sendProtocol(protocolStr);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
