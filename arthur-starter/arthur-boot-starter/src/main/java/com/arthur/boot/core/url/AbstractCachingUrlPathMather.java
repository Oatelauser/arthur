package com.arthur.boot.core.url;

/**
 * 缓存URL匹配结果
 *
 * @author DearYang
 * @date 2022-08-16
 * @since 1.0
 */
public abstract class AbstractCachingUrlPathMather extends UrlPathMather.AbstractUrlPathMather {

	private final UrlPathMather delegate;

	public AbstractCachingUrlPathMather(UrlPathMather urlPathMather) {
		this.delegate = urlPathMather;
	}

	@Override
	public boolean test(String pattern, String path) {
		Boolean matches;
		if ((matches = getCache(pattern, path)) != null) {
			return matches;
		}

		boolean result = delegate.matches(pattern, path);
		cache(pattern, path, result);
		return result;
	}

	/**
	 * 获取缓存
	 *
	 * @param pattern 待匹配的URL表达式
	 * @param path    url path
	 * @return yes or not
	 */
	public abstract Boolean getCache(String pattern, String path);

	/**
	 * 缓存匹配结果
	 *
	 * @param pattern 待匹配的URL表达式
	 * @param path    url path
	 * @param matches 委托者匹配结果
	 */
	public abstract void cache(String pattern, String path, boolean matches);

}
