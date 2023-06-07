package com.c2ray.idea.plugin.sqllog.core;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author c2ray
 */
public class AttacherFactory {
    List<Attacher> attachers = new ArrayList<>();

    private final Instrumentation inst;

    public AttacherFactory(Instrumentation inst) {
        this.inst = inst;
    }

    public AttacherFactory addAttacher(Attacher attacher) {
        attachers.add(attacher);
        return this;
    }

    public void doAttach() throws UnmodifiableClassException {
        for (Attacher attacher : attachers) {
            // 无论该class是否被加载过, 都加载一次
            try {
                Class<?> targetClass = Class.forName(attacher.getTargetClassName());
                // 如果canRetransform参数为true, 则可以对已经被加载过的class进行重定义
                // 如果为false, 则只能对还未被加载过的class进行重定义, 否则会抛出UnmodifiableClassException
                inst.addTransformer(attacher, true);
                inst.retransformClasses(targetClass);
            } catch (ClassNotFoundException e) {
                // do nothing
            }
        }

    }

}
