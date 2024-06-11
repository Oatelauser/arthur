package com.arthur.boot.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 基于class对象进行命名
 * <p>
 * 优化{@code org.springframework.cloud.gateway.support.NameUtils}代码，加入自定义类命令
 *
 * @author DearYang
 * @date 2022-09-06
 * @since 1.0
 */
@SuppressWarnings({"unused", "DuplicatedCode"})
public abstract class NameUtils {

	private static final Pattern NAME_PATTERN = Pattern.compile("([A-Z][a-z0-9]+)");

	public static <T, U extends T> String normalizeClassName(Class<T> superclass, Class<U> subclass) {
		return normalizeClassName(superclass.getSimpleName(), subclass.getSimpleName());
	}

	public static String normalizeClassName(String superclassName, String subclassName) {
		return removeGarbage(subclassName.replace(superclassName, ""));
	}

	public static String normalizeClassNameAsProperty(String superclassName, String subclassName) {
		return normalizeToCanonicalPropertyFormat(normalizeClassName(superclassName, subclassName));
	}

	public static <T, U extends T> String normalizeClassNameAsProperty(Class<T> superclass, Class<U> subclass) {
		return normalizeToCanonicalPropertyFormat(normalizeClassName(superclass, subclass));
	}

	public static String normalizeToCanonicalPropertyFormat(String name) {
		Matcher matcher = NAME_PATTERN.matcher(name);
		StringBuilder builder = new StringBuilder();
		while (matcher.find()) {
			if (builder.length() != 0) {
				matcher.appendReplacement(builder, "-" + matcher.group(1));
			} else {
				matcher.appendReplacement(builder, matcher.group(1));
			}
		}
		return builder.toString().toLowerCase();
	}

	public static String removeGarbage(String s) {
		int garbageIdx = s.indexOf("$Mockito");
		if (garbageIdx > 0) {
			return s.substring(0, garbageIdx);
		}

		return s;
	}

}
