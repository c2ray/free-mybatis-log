package com.c2ray.idea.plugin.sqllog.service;

import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.project.Project;

import javax.swing.*;

/**
 * @author c2ray
 * @since 2023/5/17
 */
public interface MybatisLogService {

    void init(Project project);

    ConsoleView getConsoleView();

    JPanel getConsolePanel();

    void printContent(String sql);

    void attachProcess(ProcessHandler processHandler);
}
