package com.arthur.cloud.openfeign.utils;

import static com.arthur.boot.utils.NameUtils.normalizeToCanonicalPropertyFormat;
import static com.arthur.boot.utils.NameUtils.removeGarbage;

/**
 * copy by {@code com.broadtech.arthur.spring.utils.NameUtils}
 *
 * @author DearYang
 * @date 2022-09-06
 * @since 1.0
 */
public abstract class NameUtils {

	public static <T, U extends T> String normalizeClassName(Class<T> superclass, Class<U> subclass) {
		return removeGarbage(subclass.getSimpleName().replace(superclass.getSimpleName(), ""));
	}

	public static <T, U extends T> String normalizeClassNameAsProperty(Class<T> superclass, Class<U> subclass) {
		return normalizeToCanonicalPropertyFormat(normalizeClassName(superclass, subclass));
	}

}
