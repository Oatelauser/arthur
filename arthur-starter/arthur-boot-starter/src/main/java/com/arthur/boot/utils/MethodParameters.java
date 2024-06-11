package com.arthur.boot.utils;

import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;

import java.lang.reflect.Type;

/**
 * {@link MethodParameter}工具类
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-04-28
 * @since 1.0
 */
public abstract class MethodParameters {

	/**
	 * 解析{@link MethodParameter}的泛型参数
	 *
	 * @param parameter {@link MethodParameter}
	 * @return 参数类型
	 */
	public static Class<?> resolveParameterGenericType(MethodParameter parameter) {
		Type parameterType = parameter.getGenericParameterType();
		if (parameterType instanceof Class<?> parameterClass) {
			return parameterClass;
		}

		ResolvableType resolvableType = ResolvableType.forMethodParameter(parameter);
		return resolvableType.resolve();
	}

}
