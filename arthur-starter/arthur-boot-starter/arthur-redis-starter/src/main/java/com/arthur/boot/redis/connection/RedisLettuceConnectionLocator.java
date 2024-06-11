package com.arthur.boot.redis.connection;

import com.arthur.common.lifecycle.ShutdownHook;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * {@link LettuceConnectionFactory}定位器
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-17
 * @since 1.0
 */
public class RedisLettuceConnectionLocator implements LettuceConnectionLocator, InitializingBean, ShutdownHook {

	private final Map<String, LettuceConnectionFactory> lettuceConnections;
	private final ThreadLocal<String> REDIS_LOCATOR = new ThreadLocal<>();

	public RedisLettuceConnectionLocator(Map<String, LettuceConnectionFactory> lettuceConnections) {
		this.lettuceConnections = lettuceConnections;
	}

	@Override
	public LettuceConnectionFactory current() {
		String lookup = REDIS_LOCATOR.get();
		return lettuceConnections.getOrDefault(lookup, lettuceConnections.get("default"));
	}

	@Override
	public LettuceConnectionFactory lookup(String routeKey) {
		return lettuceConnections.get(routeKey);
	}

	@Override
	public void switchTo(String name) {
		if (lettuceConnections.containsKey(name)) {
			REDIS_LOCATOR.set(name);
		}
	}

	@Override
	public void afterPropertiesSet() {
		if (!CollectionUtils.isEmpty(lettuceConnections)) {
			lettuceConnections.values().forEach(LettuceConnectionFactory::afterPropertiesSet);
		}
	}

	@Override
	public void shutdown() {
		if (!CollectionUtils.isEmpty(lettuceConnections)) {
			lettuceConnections.values().forEach(LettuceConnectionFactory::destroy);
		}
	}

}
