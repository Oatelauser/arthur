package com.arthur.boot.redis.connection;

import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

/**
 * {@link LettuceConnectionFactory}定位器
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-17
 * @since 1.0
 */
public interface LettuceConnectionLocator {

	/**
	 * 获取当前的{@link LettuceConnectionFactory}
	 *
	 * @return {@link LettuceConnectionFactory}
	 */
	LettuceConnectionFactory current();

	/**
	 * 根据路由键查找{@link LettuceConnectionFactory}
	 *
	 * @param routeKey Redis路由键
	 * @return {@link LettuceConnectionFactory}
	 */
	LettuceConnectionFactory lookup(String routeKey);

	/**
	 * 切换路由键
	 *
	 * @param routeKey Redis路由键
	 */
	void switchTo(String routeKey);

}
