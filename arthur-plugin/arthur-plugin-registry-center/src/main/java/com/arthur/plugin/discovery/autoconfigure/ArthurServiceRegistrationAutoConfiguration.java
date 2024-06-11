package com.arthur.plugin.discovery.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.cloud.client.CommonsClientAutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * 服务注册自动配置类
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-01
 * @since 1.0
 */
@Import(ConcurrentDiscoveryClientConfiguration.class)
@AutoConfiguration(after = CommonsClientAutoConfiguration.class)
public class ArthurServiceRegistrationAutoConfiguration {



}
