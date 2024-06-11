package com.arthur.plugin.discovery.autoconfigure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.cloud.client.discovery.simple.SimpleDiscoveryProperties;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 实现并发安全的静态服务注册
 * <p>
 * 参考{@link org.springframework.cloud.client.discovery.simple.SimpleDiscoveryClientAutoConfiguration}
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-01
 * @since 1.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(SimpleDiscoveryProperties.class)
public class ConcurrentDiscoveryClientConfiguration implements ApplicationListener<WebServerInitializedEvent> {

    private int port = 0;
    private final InetUtils inet;
    private final ServerProperties server;

    private final ConcurrentDiscoveryProperties simple = new ConcurrentDiscoveryProperties();

    public ConcurrentDiscoveryClientConfiguration(ServerProperties server, InetUtils inet) {
        this.server = server;
        this.inet = inet;
    }

    @Bean
    @ConditionalOnMissingBean
    public ConcurrentDiscoveryProperties simpleDiscoveryProperties(
            @Value("${spring.application.name:application}") String serviceId) {
        simple.getLocal().setServiceId(serviceId);
        simple.getLocal().setHost(inet.findFirstNonLoopbackHostInfo().getHostname());
        simple.getLocal().setPort(findPort());
        return simple;
    }

    private int findPort() {
        if (port > 0) {
            return port;
        }
        if (server != null && server.getPort() != null && server.getPort() > 0) {
            return server.getPort();
        }
        return 8080;
    }

    @Override
    public void onApplicationEvent(WebServerInitializedEvent webServerInitializedEvent) {
        port = webServerInitializedEvent.getWebServer().getPort();
        if (port > 0) {
            simple.getLocal().setHost(inet.findFirstNonLoopbackHostInfo().getHostname());
            simple.getLocal().setPort(port);
        }
    }

}
