package com.c2ray.idea.plugin.sqllog.printer;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author c2ray
 */
public class MessageClient {

    private static final int timeout = 10;

    private static final String hostname = "localhost";

    private final InetSocketAddress targetAddr;

    public MessageClient(Integer port) {
        targetAddr = new InetSocketAddress(hostname, port);
    }

    public void send(String msg) {
        try (Socket socket = new Socket()) {
            socket.connect(targetAddr, timeout);
            socket.getOutputStream().write(msg.getBytes());
        } catch (IOException e) {
            // 连接失败 do nothing
        }
    }

    public void send(Object obj) {
        send(new Gson().toJson(obj));
    }

}
