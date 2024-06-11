package com.arthur.boot.redis.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisConnectionFactoryConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisMultiProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

/**
 * Redis自动配置类
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-17
 * @since 1.0
 */
@AutoConfiguration
@Import(RedisConnectionFactoryConfiguration.class)
@EnableConfigurationProperties(RedisMultiProperties.class)
public class ArthurBootRedisAutoConfiguration {


}
