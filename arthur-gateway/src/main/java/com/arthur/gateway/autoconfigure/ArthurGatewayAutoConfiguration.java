package com.arthur.gateway.autoconfigure;

import com.arthur.boot.core.url.UrlPathMather;
import com.arthur.gateway.filter.*;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.embedded.netty.NettyServerCustomizer;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.reactive.DispatcherHandler;
import org.springframework.web.server.WebFilter;
import reactor.netty.http.server.logging.AccessLogFactory;

/**
 * Arthur Spring Configuration
 *
 * @author DearYang
 * @date 2022-07-17
 * @since 1.0
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(value = {ArthurProperties.class, NettyTcpProperties.class})
public class ArthurGatewayAutoConfiguration {

    /**
     * Local dispatcher filter web filter.
     *
     * @param dispatcherHandler the dispatcher handler
     * @param arthurProperties  the global Config
     * @return the web filter
     */
    @Bean
    @Order(-200)
    @ConditionalOnProperty(name = "arthur.local.enable", havingValue = "true")
    public WebFilter localDispatcherFilter(final ArthurProperties arthurProperties, final DispatcherHandler dispatcherHandler, final UrlPathMather pathMather) {
        return new LocalDispatcherFilter(arthurProperties.getLocal().getLocalSecretKey(), dispatcherHandler, pathMather);
    }

    /**
     * Cross filter web filter.
     * if you application has cross-domain.
     * this is demo.
     * 1. Customize webflux cross-domain requests.
     * 2. Spring bean Sort is greater than -1.
     *
     * @param arthurProperties the arthur config
     * @return the web filter
     */
    @Bean
    @Order(-100)
    @ConditionalOnProperty(name = "arthur.cross.enabled", havingValue = "true")
    public WebFilter crossFilter(final ArthurProperties arthurProperties) {
        return new CrossFilter(arthurProperties.getCross());
    }

    /**
     * Exclude filter web filter.
     *
     * @param arthurProperties the arthur config
     * @return the web filter
     */
    @Bean
    @Order(-5)
    @ConditionalOnProperty(name = "arthur.exclude.enabled", havingValue = "true")
    public WebFilter excludeFilter(final ArthurProperties arthurProperties, UrlPathMather urlPathMather) {
        return new ExcludeFilter(arthurProperties.getExclude().getPaths(), urlPathMather);
    }

    /**
     * fallback filter web filter.
     *
     * @param arthurProperties the global Config
     * @return the fallback web filter
     */
    @Bean
    @Order(-5)
    @ConditionalOnProperty(name = "arthur.fallback.enabled", havingValue = "true")
    public WebFilter fallbackFilter(final ArthurProperties arthurProperties, UrlPathMather urlPathMather) {
        return new FallbackFilter(arthurProperties.getFallback().getPath(), urlPathMather);
    }

    /**
     * multipart/form-data file size limit
     *
     * @param arthurProperties the global Config
     * @return the web filter
     */
    @Bean
    @Order(-10)
    @ConditionalOnProperty(name = "arthur.file.enabled", havingValue = "true")
    public WebFilter fileSizeFilter(final ArthurProperties arthurProperties) {
        return new FileSizeFilter(arthurProperties.getFile().getMaxSize());
    }

    /**
     * spring webflux request and response access log
     *
     * @param arthurProperties the global Config
     * @return the gateway global filter
     */
    //@Bean
    @Order(160)
    //@ConditionalOnProperty(name = "arthur.accesslog.enabled", havingValue = "true")
    public GlobalFilter reactiveAccessLogFilter(final ArthurProperties arthurProperties) {
        return new ReactiveAccessLogFilter();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "arthur.accesslog.enabled", havingValue = "true")
    public AccessLogFactory accessLogFactory() {
        return new NettyAccessLogFactory();
    }

    /**
     * netty tcp config
     *
     * @param nettyTcpProperties config properties
     * @return customize nett tcp
     * @see NettyReactiveWebServerFactory
     */
    @Bean
    @ConditionalOnProperty(value = "netty.tcp.webServerFactoryEnabled", havingValue = "true", matchIfMissing = true)
    public NettyServerCustomizer nettyServerCustomizer(NettyTcpProperties nettyTcpProperties, ObjectProvider<AccessLogFactory> accessLog) {
        return new ConfigurationNettyCustomizer(nettyTcpProperties, accessLog);
    }

}
