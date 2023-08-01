package com.c2ray.idea.plugin.sqllog.core;

import javassist.CtClass;
import javassist.CtMethod;

import java.security.ProtectionDomain;

/**
 * @author c2ray
 */
public class MybatisMapperProxyAttacher extends Attacher {

    @Override
    public String getTargetClassName() {
        return "org.apache.ibatis.binding.MapperProxy";
    }

    @Override
    protected byte[] doTransform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                              ProtectionDomain protectionDomain, byte[] classfileBuffer) throws Exception {
        CtClass targetClass = getTargetClass();
        CtMethod targetMethod = targetClass.getDeclaredMethod("invoke");
        targetMethod.insertBefore("com.c2ray.idea.plugin.sqllog.utils.ThreadLocalUtils.setMybatisSqlId(com.c2ray.idea.plugin.sqllog.utils.ReflecUtils.getMethodStr($2));");
        return targetClass.toBytecode();
    }

}
