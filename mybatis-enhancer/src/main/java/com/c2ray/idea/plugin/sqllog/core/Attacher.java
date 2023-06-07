package com.c2ray.idea.plugin.sqllog.core;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

/**
 * @author c2ray
 */
public abstract class Attacher implements ClassFileTransformer {

    /**
     * Any way to support fuzzy matching? The restriction now is that we can not scan all the
     * dependencies conveniently.
     */
    abstract String getTargetClassName();

    protected abstract byte[] doTransform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                                          ProtectionDomain protectionDomain, byte[] classfileBuffer) throws Exception;

    final boolean matchClassName(String className) {
        assert className != null;
        String targetClassName = getTargetClassName().replace(".", "/");
        return className.equals(targetClassName);
    }

    @Override
    public final byte[] transform(ClassLoader loader, String className,
                                  Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
                                  byte[] classfileBuffer) {
        if (!matchClassName(className))
            return null;

        try {
            return doTransform(loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
