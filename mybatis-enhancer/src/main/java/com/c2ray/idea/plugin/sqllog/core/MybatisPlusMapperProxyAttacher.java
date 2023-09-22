package com.c2ray.idea.plugin.sqllog.core;

import javassist.CtClass;
import javassist.CtMethod;

import java.security.ProtectionDomain;

/**
 * @author c2ray
 */
public class MybatisPlusMapperProxyAttacher extends Attacher {

    @Override
    public String getTargetClassName() {
        return "com.baomidou.mybatisplus.core.override.MybatisMapperProxy";
    }

    @Override
    protected byte[] doTransform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                              ProtectionDomain protectionDomain, byte[] classfileBuffer) throws Exception {
        CtClass targetClass = getTargetClass();
        CtMethod targetMethod = targetClass.getDeclaredMethod("invoke");
        targetMethod.insertBefore(
                "if (!$2.getDeclaringClass().equals(java.lang.Object.class)) {com.c2ray.idea.plugin.sqllog.utils.ThreadLocalUtils.setMybatisPlusSqlId(com.c2ray.idea.plugin.sqllog.utils.ReflectUtils.getMethodStr($2));}");
        return targetClass.toBytecode();
    }

}
