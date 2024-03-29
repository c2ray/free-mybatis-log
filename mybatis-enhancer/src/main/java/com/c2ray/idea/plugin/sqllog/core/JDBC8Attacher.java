package com.c2ray.idea.plugin.sqllog.core;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.security.ProtectionDomain;

/**
 * 兼容JDBC 8以后的版本版本
 *
 * @author c2ray
 * @since 2023/6/6
 */
@Deprecated
public class JDBC8Attacher extends Attacher {

    @Override
    public String getTargetClassName() {
        return "com.mysql.cj.jdbc.ClientPreparedStatement";
    }

    @Override
    protected byte[] doTransform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                                 ProtectionDomain protectionDomain, byte[] classfileBuffer) throws Exception {
        ClassPool classPool = ClassPool.getDefault();
        CtClass targetClass = classPool.get(getTargetClassName());
        CtMethod targetMethod = targetClass.getDeclaredMethod("execute");

        targetMethod.insertBefore(
                "String sql = com.c2ray.idea.plugin.sqllog.utils.SqlUtils.getSql($0);\n" +
                        "sql = com.c2ray.idea.plugin.sqllog.utils.StringUtils.removeExtraWhitespaces(sql);\n" +
                        "com.c2ray.idea.plugin.sqllog.utils.MessageUtils.sendMybatisProtocol(sql);");
        return targetClass.toBytecode();
    }

}
