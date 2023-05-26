package com.c2ray.idea.plugin.core.printer;


import com.c2ray.idea.plugin.core.formatter.BasicFormatter;
import com.c2ray.idea.plugin.core.protocol.SqlLogProtocol;
import com.c2ray.idea.plugin.core.utils.ProtocolUtils;

import java.io.IOException;

/**
 * @author c2ray
 */
public abstract class AbsMessager {

    private final String frameworkName;

    private final MessageClient messageClient;

    private static final BasicFormatter FORMATTER = new BasicFormatter();

    public AbsMessager(String frameworkName, Integer port) {
        this.frameworkName = frameworkName;
        this.messageClient = new MessageClient(port);
    }

    public void sendProtocol(String sql) throws IOException {
        SqlLogProtocol protocol = ProtocolUtils.getProtocol(frameworkName, sql);
        messageClient.send(protocol);
    }

}
