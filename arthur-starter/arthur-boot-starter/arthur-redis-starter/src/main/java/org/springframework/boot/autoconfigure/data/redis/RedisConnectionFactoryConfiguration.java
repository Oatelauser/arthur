package org.springframework.boot.autoconfigure.data.redis;

import com.arthur.boot.redis.connection.MultipleReactiveRedisLettuceConnectionFactory;
import com.arthur.boot.redis.connection.MultipleRedisLettuceConnectionFactory;
import io.lettuce.core.resource.ClientResources;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Redis多数据源连接工厂配置类
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-19
 * @since 1.0
 */
@Configuration(proxyBeanMethods = false)
public class RedisConnectionFactoryConfiguration {

    @Bean
    @ConditionalOnClass(RedisConnectionFactory.class)
    public MultipleRedisLettuceConnectionFactory redisLettuceConnectionFactory(
            RedisMultiProperties properties, ClientResources clientResources,
            ObjectProvider<RedisClusterConfiguration> clusterConfiguration,
            ObjectProvider<RedisSentinelConfiguration> sentinelConfiguration,
            ObjectProvider<RedisStandaloneConfiguration> standaloneConfiguration,
            ObjectProvider<LettuceClientConfigurationBuilderCustomizer> customizer) {
        Map<String, LettuceConnectionFactory> lettuceConnections = this.lettuceConnections(
                properties, clientResources, clusterConfiguration, sentinelConfiguration,
                standaloneConfiguration, customizer);
        return new MultipleRedisLettuceConnectionFactory(lettuceConnections);
    }

    @Bean
    @ConditionalOnClass(ReactiveRedisConnectionFactory.class)
    public MultipleReactiveRedisLettuceConnectionFactory reactiveRedisLettuceConnectionFactory(
            RedisMultiProperties properties,
            ClientResources clientResources,
            ObjectProvider<RedisClusterConfiguration> clusterConfiguration,
            ObjectProvider<RedisSentinelConfiguration> sentinelConfiguration,
            ObjectProvider<RedisStandaloneConfiguration> standaloneConfiguration,
            ObjectProvider<LettuceClientConfigurationBuilderCustomizer> customizer) {
        Map<String, LettuceConnectionFactory> lettuceConnections = this.lettuceConnections(
                properties, clientResources, clusterConfiguration, sentinelConfiguration,
                standaloneConfiguration, customizer);
        return new MultipleReactiveRedisLettuceConnectionFactory(lettuceConnections);
    }

    private Map<String, LettuceConnectionFactory> lettuceConnections(
            RedisMultiProperties properties, ClientResources clientResources,
            ObjectProvider<RedisClusterConfiguration> clusterConfiguration,
            ObjectProvider<RedisSentinelConfiguration> sentinelConfiguration,
            ObjectProvider<RedisStandaloneConfiguration> standaloneConfiguration,
            ObjectProvider<LettuceClientConfigurationBuilderCustomizer> customizer) {
        Map<String, LettuceConnectionFactory> lettuceConnections = new HashMap<>();
        for (Map.Entry<String, RedisProperties> entry : properties.getMulti().entrySet()) {
            LettuceConnectionConfiguration configuration = new LettuceConnectionConfiguration(entry.getValue(),
                    standaloneConfiguration, sentinelConfiguration, clusterConfiguration);
            LettuceConnectionFactory lettuceConnectionFactory = configuration
                    .redisConnectionFactory(customizer, clientResources);
            lettuceConnections.put(entry.getKey(), lettuceConnectionFactory);
        }
        return lettuceConnections;
    }

}
