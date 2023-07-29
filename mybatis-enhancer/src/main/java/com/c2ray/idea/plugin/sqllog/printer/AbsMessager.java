package com.c2ray.idea.plugin.sqllog.printer;


import com.c2ray.idea.plugin.sqllog.formatter.BasicFormatter;
import com.c2ray.idea.plugin.sqllog.protocol.SqlLogProtocol;
import com.c2ray.idea.plugin.sqllog.utils.ProtocolUtils;

import java.io.IOException;

/**
 * @author c2ray
 */
public abstract class AbsMessager {

    private final String frameworkName;

    private final MessageClient messageClient;
    
    public AbsMessager(String frameworkName, Integer port) {
        this.frameworkName = frameworkName;
        this.messageClient = new MessageClient(port);
    }


    public void sendProtocol(Integer status) {
        sendProtocol(status, null, null);
    }

    public void sendProtocol(Integer status, String methodName, String sql) {
        SqlLogProtocol protocol = ProtocolUtils.getProtocol(frameworkName, status, methodName, sql);
        messageClient.send(protocol);
    }

}
