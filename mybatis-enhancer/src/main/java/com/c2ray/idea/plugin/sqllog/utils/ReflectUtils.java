package com.c2ray.idea.plugin.sqllog.utils;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author c2ray
 * @since 2023/7/27
 */
public class ReflectUtils {

    private static final String TYPE_EXPRESSION = "[a-z0-9.]*(?=\\.\\w+)";


    public static String getMethodStr(Method method) {
        String clzName = method.getDeclaringClass().getName();
        String methodName = method.getName();
        String params = Arrays.stream(method.getGenericParameterTypes())
                .map(Type::getTypeName).map(ReflectUtils::simplifyTypeName)
                .collect(Collectors.joining(", "));
        return String.format("%s#%s(%s)", clzName, methodName, params);
    }

    private static String simplifyTypeName(String typeName) {
        return typeName.replaceAll(TYPE_EXPRESSION, "")
                .replaceAll("\\.", "");
    }

}
