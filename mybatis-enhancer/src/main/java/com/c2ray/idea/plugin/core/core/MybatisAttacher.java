package com.c2ray.idea.plugin.core.core;

import javassist.*;

import java.io.IOException;
import java.security.ProtectionDomain;

/**
 * @author c2ray
 */
public class MybatisAttacher implements Attacher {

    @Override
    public String getTargetClassName() {
        return "org.apache.ibatis.executor.SimpleExecutor";
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        if (!getTargetClassName().replace(".", "/").equals(className)) return null;
        ClassPool classPool = ClassPool.getDefault();
        try {
            CtClass targetClass = classPool.get(getTargetClassName());
            CtMethod targetMethod = targetClass.getDeclaredMethod("prepareStatement");
               targetMethod.insertAfter(
                    "String sql = $_.unwrap(java.sql.PreparedStatement.class).toString().split(\"[:|-]\", 2)[1];\n" +
                            "sql = com.c2ray.idea.plugin.core.utils.StringUtils.removeExtraWhitespaces(sql);\n" +
                            "com.c2ray.idea.plugin.core.utils.MessageUtils.sendMybatisProtocol(sql);" +
                            "return $_;");
            return targetClass.toBytecode();
        } catch (NotFoundException | CannotCompileException | IOException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
