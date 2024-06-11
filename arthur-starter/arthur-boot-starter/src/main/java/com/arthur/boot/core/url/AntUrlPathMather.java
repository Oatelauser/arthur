package com.arthur.boot.core.url;

import org.springframework.util.AntPathMatcher;

/**
 * Ant URL Path匹配器
 *
 * @author DearYang
 * @date 2022-08-16
 * @since 1.0
 */
public class AntUrlPathMather extends UrlPathMather.AbstractUrlPathMather {

	private final AntPathMatcher antPathMatcher = new AntPathMatcher();

	@Override
	public boolean test(String pattern, String path) {
		return antPathMatcher.match(pattern, path);
	}

}
