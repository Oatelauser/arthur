package com.arthur.boot.aop;

import org.springframework.lang.NonNull;
import org.springframework.util.ReflectionUtils.FieldFilter;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * private类实例变量过滤器
 *
 * @author DearYang
 * @date 2022-09-28
 * @since 1.0
 */
public class PrivateFieldFilter implements FieldFilter {

	/**
	 * 是否静态变量
	 */
	private final boolean staticOnly;

	public PrivateFieldFilter() {
		this(false);
	}

	public PrivateFieldFilter(boolean staticOnly) {
		this.staticOnly = staticOnly;
	}

	@Override
	public boolean matches(@NonNull Field field) {
		int mod = field.getModifiers();
		return staticOnly == Modifier.isStatic(mod) && Modifier.isPrivate(mod);
	}

}
