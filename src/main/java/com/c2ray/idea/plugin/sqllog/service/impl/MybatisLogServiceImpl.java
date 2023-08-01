package com.c2ray.idea.plugin.sqllog.service.impl;

import com.c2ray.idea.plugin.sqllog.service.MybatisLogService;
import com.c2ray.idea.plugin.sqllog.utils.ActionUtils;
import com.c2ray.idea.plugin.sqllog.utils.ProcessUtils;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.util.Disposer;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

/**
 * 生命周期:                               <br/>
 * init -> attach -> detach               <br/>
 * 这个类会在`MyBatisLogToolWindow`激活时加载
 *
 * @author c2ray
 * @since 2023/5/17
 */
@Service(Service.Level.PROJECT)
@State(name = "MybatisLogService", storages = @Storage(StoragePathMacros.PRODUCT_WORKSPACE_FILE))
public final class MybatisLogServiceImpl implements PersistentStateComponent<MybatisLogServiceImpl.State>,
        MybatisLogService, Disposable {

    private State state = new State();

    private ConsoleView consoleView;

    private JPanel consolePanel;

    private ProcessHandler processHandler;

    private SimpleToolWindowPanel toolWindowPanel;

    private static final String EOF = "\n===========================================================================\n";

    public boolean isAttaching() {
        return ProcessUtils.isRunning(processHandler);
    }

    public ProcessHandler getProcessHandler() {
        return processHandler;
    }

    public void setProcessHandler(ProcessHandler processHandler) {
        this.processHandler = processHandler;
    }

    @Override
    public void detach() {
        setProcessHandler(null);
    }

    /**
     * 这个方法应该只被调用一次
     *
     * @param project
     */
    @Override
    public synchronized void init(Project project) {
        if (consoleView != null) {
            throw new RuntimeException("MybatisLogService has been initialized already");
        }
        ConsoleView consoleView = TextConsoleBuilderFactory.getInstance().createBuilder(project).getConsole();
        JPanel consolePanel = new JPanel();
        consolePanel.setLayout(new BorderLayout());
        consolePanel.add(consoleView.getComponent(), BorderLayout.CENTER);
        this.consoleView = consoleView;
        this.consolePanel = consolePanel;
        // action group
        final DefaultActionGroup actionGroup = new DefaultActionGroup();
        AnAction mybatisAttachRecentProcessAction = ActionUtils.getAction(ActionUtils.ActionEnum.MYBATIS_ATTACH_RECERNT_PROCESS_ACTION);
        actionGroup.add(mybatisAttachRecentProcessAction);
        actionGroup.addAll(consoleView.createConsoleActions());
        // tool bar
        ActionToolbar actionToolbar = ActionManager.getInstance().createActionToolbar("EventLog", actionGroup, false);
        actionToolbar.setTargetComponent(consolePanel);
        SimpleToolWindowPanel toolWindowPanel = new SimpleToolWindowPanel(false, true);
        toolWindowPanel.setContent(consolePanel);
        toolWindowPanel.setToolbar(actionToolbar.getComponent());
        toolWindowPanel.setOpaque(true);
        this.toolWindowPanel = toolWindowPanel;
    }

    @Override
    public synchronized ConsoleView getConsoleView() {
        if (consoleView == null)
            throw new RuntimeException("MybatisLogService has not been initialized yet");
        return consoleView;
    }

    @Override
    public JPanel getConsolePanel() {
        if (consolePanel == null)
            throw new RuntimeException("MybatisLogService has not been initialized yet");
        return consolePanel;
    }

    public void printPlainContent(String content) {
        getConsoleView().print(content + "\n\n",
                ConsoleViewContentType.NORMAL_OUTPUT);
    }

    public void printErrorContent(String content) {
        getConsoleView().print(content + "\n\n",
                ConsoleViewContentType.ERROR_OUTPUT);
    }

    @Override
    public void printContent(String content) {
        getConsoleView().print(content + EOF,
                ConsoleViewContentType.NORMAL_OUTPUT);
    }

    public void printOnAttach(String content) {
        // 清空控制台
        getConsoleView().clear();
        getConsoleView().print(content + EOF,
                ConsoleViewContentType.NORMAL_OUTPUT);
    }

    @Override
    public void attachProcess(ProcessHandler processHandler) {
        String appName = ProcessUtils.getAppName(processHandler);
        String content = String.format("Sql from %s will be printed.", appName);
        printOnAttach(content);
        this.processHandler = processHandler;
    }

    @Override
    public SimpleToolWindowPanel getToolWindowPanel() {
        return toolWindowPanel;
    }

    @Override
    public void dispose() {
        Disposer.dispose(consoleView);
    }

    @Override
    public @NotNull State getState() {
        return state;
    }

    @Override
    public void loadState(@NotNull State state) {
        this.state = state;
    }


    public static class State {

        /**
         * 项目名称
         */
        private String projectName;

        public String getProjectName() {
            return projectName;
        }

        public void setProjectName(String projectName) {
            this.projectName = projectName;
        }
    }

}
