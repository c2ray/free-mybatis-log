package com.c2ray.idea.plugin.sqllog.service.impl;

import com.c2ray.idea.plugin.sqllog.LogServer;
import com.c2ray.idea.plugin.sqllog.formatter.BasicFormatter;
import com.c2ray.idea.plugin.sqllog.protocol.SqlLogProtocol;
import com.c2ray.idea.plugin.sqllog.protocol.SqlLogProtocolStatusEnum;
import com.c2ray.idea.plugin.sqllog.service.SqlLogService;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.annotations.Transient;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.c2ray.idea.plugin.sqllog.constant.Message.PROJECT_DOWN_MSG;

/**
 * @author c2ray
 * @since 2023/5/18
 */
@Service(Service.Level.APP)
@State(name = "SqlLogService", storages = @Storage(StoragePathMacros.CACHE_FILE))
public final class SqlLogServiceImpl implements PersistentStateComponent<SqlLogServiceImpl.State>,
        SqlLogService, Disposable {

    private LogServer logServer;

    private State state = new State();

    private final Map<Integer, MybatisLogServiceImpl> PID_MYBATISLOGSERVICE_MAP = new HashMap<>();

    private static final BasicFormatter BASIC_FORMATTER = new BasicFormatter();

    private void launchLogServer() {
        try {
            this.logServer = new LogServer();
            int serverPort = logServer.getPort();
            state.setServerPort(serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Start server failed!", e);
        }
    }

    @Override
    public void init() {
        if (state.isInit())
            return;
        launchLogServer();
        state.setInit(true);
    }

    @Override
    public void register(int pid, Project project) {
        PID_MYBATISLOGSERVICE_MAP.put(pid, project.getService(MybatisLogServiceImpl.class));
    }

    @Override
    public void printProtocol(SqlLogProtocol protocol) {
        Integer pid = protocol.getPid();
        String sql = protocol.getSql();
        Integer status = protocol.getStatus();
        String methodName = protocol.getMethodName();
        MybatisLogServiceImpl mybatisLogService = PID_MYBATISLOGSERVICE_MAP.get(pid);
        if (SqlLogProtocolStatusEnum.TERMINATE.getCode().equals(status)) {
            mybatisLogService.printErrorContent(PROJECT_DOWN_MSG);
        } else {
            mybatisLogService.printPlainContent(methodName);
            mybatisLogService.printContent(BASIC_FORMATTER.format(sql));
        }
    }


    @Override
    @NotNull
    public SqlLogServiceImpl.State getState() {
        return state;
    }

    @Override
    public void loadState(@NotNull SqlLogServiceImpl.State state) {
        this.state = state;
    }

    @Override
    public void dispose() {
        try {
            logServer.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static class State {

        static final int DEFAULT_SERVER_PORT = 11190;

        private boolean init;

        private int serverPort = DEFAULT_SERVER_PORT;

        @Transient
        public void setInit(boolean init) {
            this.init = init;
        }

        public boolean isInit() {
            return init;
        }

        public Integer getServerPort() {
            return serverPort;
        }

        public void setServerPort(Integer serverPort) {
            this.serverPort = serverPort;
        }
    }
}
