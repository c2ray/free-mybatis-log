package com.c2ray.idea.plugin.sqllog.utils;

import java.sql.PreparedStatement;

/**
 * @author c2ray
 * @since 2023/6/6
 */
public class SqlUtils {


    public static String getSql(PreparedStatement statement) {
        String statementString = statement.toString();
        String sql = statementString.split("[:|-]", 2)[1];
        return sql;
    }

}
