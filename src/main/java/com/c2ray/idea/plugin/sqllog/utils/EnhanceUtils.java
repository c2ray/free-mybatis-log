package com.c2ray.idea.plugin.sqllog.utils;

import com.intellij.openapi.diagnostic.Logger;
import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author c2ray
 * @since 2023/5/24
 */
public class EnhanceUtils {

    private static final Logger log = Logger.getInstance(EnhanceUtils.class);

    private static final String ATTACH_VERSION_EXCEPTION_MESSAGE = "0";

    private static final String MYBATIS_ENHANCER_JAR_NAME = "mybatis-enhancer";

    private static final String JAR_PATH = "./libs";

    private static final int MAX_WAITING_TIME = 2500;

    public static VirtualMachine attachMybatisEnhancer(int pid, Integer serverPort, String projectName) {
        try {
            VirtualMachine vm = VirtualMachine.attach(String.valueOf(pid));
            try {
                String mybatisEnhancerJarName = String.format("%s-%s", MYBATIS_ENHANCER_JAR_NAME,
                        UUID.randomUUID().toString().replace("-", ""));
                String agentLocation = String.join("/", JAR_PATH, MYBATIS_ENHANCER_JAR_NAME + ".jar");
                InputStream jarStream = EnhanceUtils.class.getClassLoader().getResourceAsStream(agentLocation);
                assert jarStream != null;
                File tmpAgent = FileUtils.createTmpFile(mybatisEnhancerJarName, ".jar", jarStream);
                String tmpAgentPath = tmpAgent.getAbsolutePath();
                String option = String.join(":", projectName, serverPort.toString());
                loadAgent(vm, tmpAgentPath, option);
                return vm;
            } catch (IOException ex) {
                return null;
            } catch (ExecutionException | InterruptedException | TimeoutException e) {
                JOptionPane.showMessageDialog(
                        null,
                        "Target project may be pending by break point!",
                        "ERROR",
                        JOptionPane.ERROR_MESSAGE);
                return null;
            }
        } catch (AttachNotSupportedException | IOException e) {
            throw new RuntimeException("Attaching failed!", e);
        }
    }

    private static void loadAgent(VirtualMachine vm, String tmpAgentPath, String option) throws InterruptedException, ExecutionException, TimeoutException {
        CompletableFuture.runAsync(() -> {
            try {
                vm.loadAgent(tmpAgentPath, option);
            } catch (AgentLoadException | AgentInitializationException | IOException ex) {
                if (ATTACH_VERSION_EXCEPTION_MESSAGE.equals(ex.getMessage())) {
                    // do nothing
                } else {
                    throw new RuntimeException("Attaching failed!", ex);
                }
            }
        }).get(MAX_WAITING_TIME, TimeUnit.MILLISECONDS);
    }

}
