package com.arthur.cloud.sleuth.filter;

import com.arthur.cloud.sleuth.wrapper.TraceablePublisherFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * sleuth trace gateway filter
 *
 * @author DearYang
 * @date 2022-07-21
 * @since 1.0
 */
public abstract class AbstractTraceableGatewayFilter implements GatewayFilter {

    private final TraceablePublisherFactory tracedPublisher;

    public AbstractTraceableGatewayFilter(TraceablePublisherFactory tracedPublisher) {
        this.tracedPublisher = tracedPublisher;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return tracedPublisher.getTracedMono(doFilter(exchange, chain), exchange);
    }

    public abstract Mono<Void> doFilter(ServerWebExchange exchange, GatewayFilterChain chain);

}
