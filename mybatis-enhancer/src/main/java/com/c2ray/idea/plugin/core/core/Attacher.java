package com.c2ray.idea.plugin.core.core;

import java.lang.instrument.ClassFileTransformer;

/**
 * @author c2ray
 */
public interface Attacher extends ClassFileTransformer {
    String getTargetClassName();
}
