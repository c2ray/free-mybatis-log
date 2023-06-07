package com.c2ray.idea.plugin.sqllog;


import com.c2ray.idea.plugin.sqllog.config.ProjectConfig;
import com.c2ray.idea.plugin.sqllog.core.AttacherFactory;
import com.c2ray.idea.plugin.sqllog.core.JDBC51Attacher;
import com.c2ray.idea.plugin.sqllog.core.JDBC8Attacher;
import com.c2ray.idea.plugin.sqllog.utils.MessageUtils;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

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

    /**
     * -javaagent:mybatis-enhancer.jar=projectName:targetPort
     *
     * @param agentArgs
     * @param inst
     */
    private static void doAttach(String agentArgs, Instrumentation inst) {
        AttacherFactory attacherFactory = new AttacherFactory(inst);
        attacherFactory.addAttacher(new JDBC51Attacher())
                .addAttacher(new JDBC8Attacher());

        try {
            attacherFactory.doAttach();
        } catch (UnmodifiableClassException e) {
            throw new RuntimeException(e);
        }

        String[] args = agentArgs.split(":");
        String projectName = args[0];
        Integer targetPort = Integer.valueOf(args[1]);

        ProjectConfig.init(projectName, targetPort);
        MessageUtils.init();
    }
}
