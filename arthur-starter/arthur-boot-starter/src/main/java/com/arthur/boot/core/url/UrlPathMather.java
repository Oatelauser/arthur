package com.arthur.boot.core.url;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;

/**
 * URL路径匹配器
 *
 * @author DearYang
 * @date 2022-08-16
 * @since 1.0
 */
public interface UrlPathMather {

	/**
	 * 是否匹配
	 *
	 * @param pattern 待匹配的URL表达式
	 * @param path    url path
	 * @return yes or not
	 */
	boolean matches(String pattern, String path);

	/**
	 * 匹配所有
	 *
	 * @param patterns 待匹配的URL表达式
	 * @param path     url path
	 * @return yes or not
	 */
	default boolean allMatch(Collection<String> patterns, String path) {
		if (CollectionUtils.isEmpty(patterns) || !StringUtils.hasText(path)) {
			return false;
		}

		return patterns.stream().allMatch(pattern -> matches(pattern, path));
	}

	/**
	 * 匹配任何一个
	 *
	 * @param patterns 待匹配的URL表达式
	 * @param path     url path
	 * @return yes or not
	 */
	default boolean anyMatch(Collection<String> patterns, String path) {
		if (CollectionUtils.isEmpty(patterns) || !StringUtils.hasText(path)) {
			return false;
		}

		for (String pattern : patterns) {
			if (this.matches(pattern, path)) {
				return true;
			}
		}

		return false;
	}

	abstract class AbstractUrlPathMather implements UrlPathMather {

		@Override
		public boolean matches(String pattern, String path) {
			if (StringUtils.hasText(pattern) && StringUtils.hasText(path)) {
				return test(pattern, path);
			}

			return false;
		}

		/**
		 * 匹配url
		 *
		 * @param pattern url表达式
		 * @param path    url路径
		 * @return yes or no
		 */
		public abstract boolean test(String pattern, String path);

	}

}
