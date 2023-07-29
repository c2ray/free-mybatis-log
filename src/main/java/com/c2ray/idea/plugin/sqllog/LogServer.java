package com.c2ray.idea.plugin.sqllog;


import com.c2ray.idea.plugin.sqllog.protocol.SqlLogProtocol;
import com.c2ray.idea.plugin.sqllog.service.impl.SqlLogServiceImpl;
import com.c2ray.idea.plugin.sqllog.utils.RandomUtil;
import com.google.gson.Gson;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.concurrent.Executors;


/**
 * @author c2ray
 */
public class LogServer implements AutoCloseable {

    private static final Logger log = Logger.getInstance(LogServer.class);

    private static final int THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors();

    private static final int MAX_TRY = 666;

    private AsynchronousServerSocketChannel serverChannel;

    private AsynchronousChannelGroup channelGroup;

    private int port;

    public LogServer() throws IOException {
        startServer();
    }

    /**
     * 尝试启动socket连接
     *
     * @return
     * @throws IOException
     */
    public AsynchronousServerSocketChannel tryChannel() throws IOException {
        AsynchronousChannelGroup group = AsynchronousChannelGroup.withThreadPool(Executors.newFixedThreadPool(THREAD_POOL_SIZE));
        AsynchronousServerSocketChannel serverChannel = group.provider().openAsynchronousServerSocketChannel(group);
        int times = 0;
        int port;
        while (times < MAX_TRY) {
            try {
                port = RandomUtil.randomPort();
                serverChannel.bind(new InetSocketAddress(port));
                this.serverChannel = serverChannel;
                this.channelGroup = group;
                this.port = port;
                log.info("Server started, listening on port " + port);
                return serverChannel;
            } catch (IOException e) {
                times++;
            }
        }
        throw new IOException("Failed to start server!");
    }


    public void startServer() throws IOException {
        // serverChannel会在close()方法中被关闭
        AsynchronousServerSocketChannel serverChannel = tryChannel();
        // 接受客户端连接并处理数据, 这里的serverChannel可以处理多个客户端连接,
        serverChannel.accept(null, new CompletionHandler<>() {
            @Override
            public void completed(AsynchronousSocketChannel client, Object attachment) {
                serverChannel.accept(null, this);
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                // client会将数据写到dst参数中, attachment参数会被传递给CompletionHandler
                client.read(buffer, buffer, new ReceiveMsgHandler(client));
            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                // log.error("Failed to accept new client connection", exc);
            }
        });
    }


    public static class ReceiveMsgHandler implements CompletionHandler<Integer, ByteBuffer> {

        AsynchronousSocketChannel client;

        public ReceiveMsgHandler(AsynchronousSocketChannel client) {
            this.client = client;
        }

        @Override
        public void completed(Integer result, ByteBuffer attachment) {
            if (result == -1) {
                try {
                    client.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
                return;
            }

            attachment.flip();
            CharBuffer charBuffer = Charset.defaultCharset().decode(attachment);
            String str = charBuffer.toString();

            SqlLogServiceImpl sqlLogService = ApplicationManager.getApplication().getService(SqlLogServiceImpl.class);
            SqlLogProtocol protocol = new Gson().fromJson(str, SqlLogProtocol.class);
            sqlLogService.printProtocol(protocol);

            attachment.clear();
            client.read(attachment, attachment, this);
        }

        @Override
        public void failed(Throwable exc, ByteBuffer attachment) {
            try {
                log.info("Connection closed.");
                client.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }


    public int getPort() {
        return port;
    }

    @Override
    public void close() throws Exception {
        channelGroup.shutdownNow();
        serverChannel.close();
    }

}
