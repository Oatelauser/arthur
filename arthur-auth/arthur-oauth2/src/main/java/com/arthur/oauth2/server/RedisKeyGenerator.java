package com.arthur.oauth2.server;

/**
 * Redis Key的生成策略
 *
 * @author DearYang
 * @date 2022-09-05
 * @since 1.0
 */
public interface RedisKeyGenerator<T> {

	/**
	 * 生成redis的key
	 *
	 * @param data 数据
	 * @return key
	 */
	String generate(T data);

}
