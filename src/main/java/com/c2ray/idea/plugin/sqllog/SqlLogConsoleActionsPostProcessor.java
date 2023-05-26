package com.c2ray.idea.plugin.sqllog;

import com.intellij.execution.ExecutionManager;
import com.intellij.execution.actions.ConsoleActionsPostProcessor;
import com.intellij.execution.impl.ConsoleViewImpl;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.actionSystem.AnAction;
import org.jetbrains.annotations.NotNull;

/**
 * @author c2ray
 */
public class SqlLogConsoleActionsPostProcessor extends ConsoleActionsPostProcessor {


    @Override
    public AnAction @NotNull [] postProcess(@NotNull ConsoleView console, AnAction @NotNull [] actions) {
        // ExecutionManager.getInstance(((ConsoleViewImpl) console).getProject()).getRunningProcesses()[0].toString();

        return super.postProcess(console, actions);
    }

}
