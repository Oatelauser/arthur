package com.arthur.boot.redis.connection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.ReactiveRedisClusterConnection;
import org.springframework.data.redis.connection.ReactiveRedisConnection;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.lang.NonNull;

import java.util.Map;

/**
 * Reactive Redis多数据源
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-17
 * @since 1.0
 */
public class MultipleReactiveRedisLettuceConnectionFactory extends RedisLettuceConnectionLocator
		implements ReactiveRedisConnectionFactory {

	public MultipleReactiveRedisLettuceConnectionFactory(Map<String, LettuceConnectionFactory> lettuceConnections) {
		super(lettuceConnections);
	}

	@NonNull
	@Override
	public ReactiveRedisConnection getReactiveConnection() {
		return current().getReactiveConnection();
	}

	@NonNull
	@Override
	public ReactiveRedisClusterConnection getReactiveClusterConnection() {
		return current().getReactiveClusterConnection();
	}

	@Override
	public DataAccessException translateExceptionIfPossible(@NonNull RuntimeException ex) {
		return current().translateExceptionIfPossible(ex);
	}

}
