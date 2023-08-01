package com.c2ray.idea.plugin.sqllog;

import com.c2ray.idea.plugin.sqllog.service.impl.MybatisLogServiceImpl;
import com.c2ray.idea.plugin.sqllog.service.impl.SqlLogServiceImpl;
import com.c2ray.idea.plugin.sqllog.utils.EnhanceUtils;
import com.c2ray.idea.plugin.sqllog.utils.IconUtils;
import com.intellij.execution.ExecutionManager;
import com.intellij.execution.process.KillableProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.util.ReflectionUtil;
import com.sun.tools.attach.VirtualMachine;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.c2ray.idea.plugin.sqllog.constant.Message.CONSOLE_DOWN_MSG;
import static com.c2ray.idea.plugin.sqllog.constant.Message.NO_RUNNING_MYBATIS_PROJECT_MSG;
import static com.c2ray.idea.plugin.sqllog.utils.IconUtils.IconEnum.MYBATIS;
import static com.c2ray.idea.plugin.sqllog.utils.IconUtils.IconEnum.STOP;
import static com.c2ray.idea.plugin.sqllog.utils.PredictUtils.notFound;
import static com.c2ray.idea.plugin.sqllog.utils.ProcessUtils.isJavaProcess;
import static com.c2ray.idea.plugin.sqllog.utils.ProcessUtils.isRunning;

/**
 * @author c2ray
 * @since 2023/5/4
 */
public class MybatisAttachRecentProcessAction extends DumbAwareAction {

    private static final Logger log = Logger.getInstance(MybatisAttachRecentProcessAction.class);

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();

        if (notFound(project))
            return;

        ProcessHandler javaProcess = getRecentRunningJavaProcess(project);
        if (notFound(javaProcess)) {
            Objects.requireNonNull(project.getServiceIfCreated(MybatisLogServiceImpl.class)).printPlainContent(NO_RUNNING_MYBATIS_PROJECT_MSG);
            return;
        }

        SqlLogServiceImpl sqlLogService = ApplicationManager.getApplication().getService(SqlLogServiceImpl.class);
        if (!sqlLogService.getState().isInit()) {
            sqlLogService.init();
        }

        doAttachMybatisEnhancer(sqlLogService, project, javaProcess, e);
    }

    private void doAttachMybatisEnhancer(SqlLogServiceImpl sqlLogService, Project project,
                                         ProcessHandler processHandler, AnActionEvent e) {
        int pid = getPid(processHandler);
        Integer serverPort = sqlLogService.getState().getServerPort();
        String projectName = project.getName();
        MybatisLogServiceImpl mybatisLogService = project.getServiceIfCreated(MybatisLogServiceImpl.class);
        assert mybatisLogService != null;

        if (sqlLogService.isAttaching(pid)) {
            mybatisLogService.printErrorContent(CONSOLE_DOWN_MSG);
            sqlLogService.detach(pid);
            mybatisLogService.detach();
            return;
        }

        VirtualMachine vm = EnhanceUtils.attachMybatisEnhancer(pid, serverPort, projectName);
        sqlLogService.register(pid, project, vm);
        mybatisLogService.attachProcess(processHandler);
        log.info("mybatis-enhancer attached to pid: " + pid);
    }

    private ProcessHandler getRecentRunningJavaProcess(Project project) {
        ProcessHandler[] runningProcesses = ExecutionManager.getInstance(project).getRunningProcesses();
        // 将最近运行的process排在前面
        List<ProcessHandler> processList = Arrays.asList(runningProcesses);
        Collections.reverse(processList);
        return processList.stream().filter(processHandler -> isRunning(processHandler) && isJavaProcess(processHandler)).findFirst().orElse(null);
    }

    private int getPid(ProcessHandler processHandler) {
        Process myProcess = ReflectionUtil.getField(KillableProcessHandler.class, processHandler, Process.class, "myProcess");
        return (int) myProcess.toHandle().pid();
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        assert project != null;
        MybatisLogServiceImpl mybatisLogService = project.getService(MybatisLogServiceImpl.class);
        if (mybatisLogService.isAttaching()) {
            e.getPresentation().setIcon(IconUtils.getIcon(STOP));
        } else {
            e.getPresentation().setIcon(IconUtils.getIcon(MYBATIS));
        }
    }

}
