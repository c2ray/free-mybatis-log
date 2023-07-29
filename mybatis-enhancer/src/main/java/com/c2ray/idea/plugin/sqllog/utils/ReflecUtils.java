package com.c2ray.idea.plugin.sqllog.utils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author c2ray
 * @since 2023/7/27
 */
public class ReflecUtils {

    public static String getMethodStr(Method method) {
        String clzName = method.getDeclaringClass().getName();
        String methodName = method.getName();
        String params = Arrays.stream(method.getParameterTypes()).map(Class::getSimpleName).collect(Collectors.joining(", "));
        return String.format("%s#%s(%s)", clzName, methodName, params);
    }

}
