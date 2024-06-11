package com.arthur.boot.aop;

import org.springframework.core.BridgeMethodResolver;
import org.springframework.lang.NonNull;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.MethodFilter;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * public的方法过滤器，排除{@link Object}的方法
 * <p>
 * 1.{@code staticOnly = true}，则是判断静态方法
 * 2.{@code staticOnly = false}，则是判断实例方法
 *
 * @author DearYang
 * @date 2022-09-28
 * @since 1.0
 */
public class PublicMethodFilter implements MethodFilter {

	/**
	 * 是否静态方法
	 */
	private final boolean staticOnly;

	public PublicMethodFilter() {
		this(false);
	}

	public PublicMethodFilter(boolean staticOnly) {
		this.staticOnly = staticOnly;
	}

	@Override
	public boolean matches(@NonNull Method method) {
		if (ReflectionUtils.isObjectMethod(method)) {
			return false;
		}

		Method bridgedMethod = BridgeMethodResolver.findBridgedMethod(method);
		if (!BridgeMethodResolver.isVisibilityBridgeMethodPair(method, bridgedMethod)) {
			return false;
		}

		int mod = method.getModifiers();
		return (staticOnly == Modifier.isStatic(mod))
			&& Modifier.isPublic(mod);
	}

}
