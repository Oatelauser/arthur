package com.arthur.lang.lambda;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 获取lambda表达式的返回值、参数
 *
 * @author DearYang
 * @date 2022-07-30
 * @see SerializedLambda
 * @since 1.0
 */
public interface SerializableLambda extends Serializable {

    Pattern RETURN_TYPE_PATTERN = Pattern.compile("\\(.*\\)L(.*);");
    Pattern PARAMETER_TYPE_PATTERN = Pattern.compile("\\((.*)\\).*");

    default SerializedLambda getSerializedLambda() {
        try {
            Method method = getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(true);
            return (SerializedLambda) method.invoke(this);
        } catch (Exception e) {
            throw new IllegalStateException("获取Lambda信息失败", e);
        }
    }

    /**
     * 获取Lambda表达式返回类型
     */
    default Class<?> getReturnType() {
        String expr = getSerializedLambda().getInstantiatedMethodType();
        Matcher matcher = RETURN_TYPE_PATTERN.matcher(expr);
        if (!matcher.find() || matcher.groupCount() != 1) {
            throw new RuntimeException("获取Lambda信息失败");
        }

        String className = matcher.group(1).replace("/", ".");
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("无法加载类", e);
        }
    }

    /**
     * 获取Lambda表达式的参数类型
     */
    default List<Class<?>> getParameterTypes() {
        String expr = getSerializedLambda().getInstantiatedMethodType();
        Matcher matcher = PARAMETER_TYPE_PATTERN.matcher(expr);
        if (!matcher.find() || matcher.groupCount() != 1) {
            throw new RuntimeException("获取Lambda信息失败");
        }

        expr = matcher.group(1);
        return Arrays.stream(expr.split(";")).filter(s -> !s.isBlank()).map(s -> s.replace("L", "").replace("/", ".")).map(s -> {
            try {
                return Class.forName(s);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("无法加载类", e);
            }
        }).collect(Collectors.toList());
    }

}
