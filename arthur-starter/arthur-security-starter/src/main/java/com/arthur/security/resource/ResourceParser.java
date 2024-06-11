package com.arthur.security.resource;

/**
 * 资源解析器
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-05-08
 * @since 1.0
 */
public interface ResourceParser<R> {

	/**
	 * 解析资源
	 *
	 * @param source 来源对象
	 * @return 资源
	 */
	String parse(R source);

}
