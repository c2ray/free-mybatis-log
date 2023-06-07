package com.c2ray.idea.plugin.sqllog.printer;

/**
 * @author c2ray
 */
public class MybatisMessager extends AbsMessager {

    public MybatisMessager(Integer targetPort) {
        super("mybatis", targetPort);
    }

}
