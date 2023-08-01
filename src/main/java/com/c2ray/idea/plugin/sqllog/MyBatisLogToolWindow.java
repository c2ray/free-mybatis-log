package com.c2ray.idea.plugin.sqllog;

import com.c2ray.idea.plugin.sqllog.service.impl.MybatisLogServiceImpl;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

/**
 * @author c2ray
 * @version 1.0
 */
public class MyBatisLogToolWindow implements ToolWindowFactory {


    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        MybatisLogServiceImpl mybatisLogService = project.getService(MybatisLogServiceImpl.class);
        mybatisLogService.init(project);

        SimpleToolWindowPanel toolWindowPanel = mybatisLogService.getToolWindowPanel();
        ContentFactory contentFactory = ApplicationManager.getApplication().getService(ContentFactory.class);
        Content content = contentFactory.createContent(toolWindowPanel.getComponent(), "", false);
        toolWindow.getContentManager().addContent(content);
    }

}
