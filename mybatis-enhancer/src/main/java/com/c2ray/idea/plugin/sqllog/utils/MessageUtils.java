package com.c2ray.idea.plugin.sqllog.utils;

import com.c2ray.idea.plugin.sqllog.config.ProjectConfig;
import com.c2ray.idea.plugin.sqllog.printer.MybatisMessager;
import com.c2ray.idea.plugin.sqllog.protocol.SqlLogProtocolStatusEnum;

/**
 * @author c2ray
 * @since 2023/5/15
 */
public class MessageUtils {

    private static MybatisMessager MYBATIS_PRINTER;

    public static void init() {
        MYBATIS_PRINTER = new MybatisMessager(ProjectConfig.getTargetPort());
    }

    public static void sendMybatisProtocol(String methodName, String sql) {
        MYBATIS_PRINTER.sendProtocol(SqlLogProtocolStatusEnum.CRUD.getCode(), methodName, sql);
    }

    public static void sendTerminate() {
        MYBATIS_PRINTER.sendProtocol(SqlLogProtocolStatusEnum.TERMINATE.getCode());
    }

}
