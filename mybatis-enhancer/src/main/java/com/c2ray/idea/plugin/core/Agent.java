package com.c2ray.idea.plugin.core;


import com.c2ray.idea.plugin.core.config.ProjectConfig;
import com.c2ray.idea.plugin.core.core.AttacherFactory;
import com.c2ray.idea.plugin.core.core.MybatisAttacher;
import com.c2ray.idea.plugin.core.utils.MessageUtils;

import java.lang.instrument.Instrumentation;

/**
 * @author c2ray
 */
public class Agent {

    static void premain(String agentArgs, Instrumentation inst) {
        doAttach(agentArgs, inst);
    }

    static void premain(String agentArgs) {
    }

    /**
     * e.g: java -jar mybatis-enhancer.jar=projectName:targetPort
     */
    static void agentmain(String agentArgs, Instrumentation inst) {
        doAttach(agentArgs, inst);
    }

    static void agentmain(String agentArgs) {
    }

    private static void doAttach(String agentArgs, Instrumentation inst) {
        AttacherFactory attacherFactory = new AttacherFactory(inst);
        attacherFactory.addAttacher(new MybatisAttacher());
        attacherFactory.doAttach();

        String[] args = agentArgs.split(":");
        String projectName = args[0];
        Integer targetPort = Integer.valueOf(args[1]);

        ProjectConfig.init(projectName, targetPort);
        MessageUtils.init();
    }
}
