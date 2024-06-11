package com.arthur.boot.redis.connection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConnection;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.lang.NonNull;

import java.util.Map;

/**
 * Redis多数据源
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-17
 * @since 1.0
 */
public class MultipleRedisLettuceConnectionFactory extends RedisLettuceConnectionLocator
		implements RedisConnectionFactory {

	public MultipleRedisLettuceConnectionFactory(Map<String, LettuceConnectionFactory> lettuceConnections) {
		super(lettuceConnections);
	}

	@NonNull
	@Override
	public RedisConnection getConnection() {
		return current().getConnection();
	}

	@NonNull
	@Override
	public RedisClusterConnection getClusterConnection() {
		return current().getClusterConnection();
	}

	@Override
	public boolean getConvertPipelineAndTxResults() {
		return current().getConvertPipelineAndTxResults();
	}

	@NonNull
	@Override
	public RedisSentinelConnection getSentinelConnection() {
		return current().getSentinelConnection();
	}

	@Override
	public DataAccessException translateExceptionIfPossible(@NonNull RuntimeException ex) {
		return current().translateExceptionIfPossible(ex);
	}

}
