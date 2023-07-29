package com.c2ray.idea.plugin.sqllog;

import com.c2ray.idea.plugin.sqllog.service.impl.MybatisLogServiceImpl;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author c2ray
 * @version 1.0
 */
public class MyBatisLogToolWindow implements ToolWindowFactory {


    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        MybatisLogServiceImpl mybatisLogService = project.getService(MybatisLogServiceImpl.class);
        mybatisLogService.init(project);
        ConsoleView consoleView = mybatisLogService.getConsoleView();
        JPanel consolePanel = mybatisLogService.getConsolePanel();
        ContentFactory contentFactory = ApplicationManager.getApplication().getService(ContentFactory.class);
         
        final DefaultActionGroup actionGroup = new DefaultActionGroup();
        AnAction mybatisAttachRecentProcessAction = ActionManager.getInstance()
                .getAction("MybatisAttachRecentProcessAction");
        actionGroup.add(mybatisAttachRecentProcessAction);
        actionGroup.addAll(consoleView.createConsoleActions());

        ActionToolbar actionToolbar = ActionManager.getInstance().createActionToolbar("EventLog", actionGroup, false);
        actionToolbar.setTargetComponent(consolePanel);
        SimpleToolWindowPanel toolWindowPanel = new SimpleToolWindowPanel(false, true);
        toolWindowPanel.setContent(consolePanel);
        toolWindowPanel.setToolbar(actionToolbar.getComponent());
        toolWindowPanel.setOpaque(true);
        Content content = contentFactory.createContent(toolWindowPanel.getComponent(), "", false);
        toolWindow.getContentManager().addContent(content);
    }

}
