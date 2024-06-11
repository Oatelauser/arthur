package com.arthur.cloud.sleuth.filter;

import com.arthur.cloud.sleuth.constant.SleuthConstant;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.TraceContext;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Spring Cloud Sleuth Filter
 *
 * @author DearYang
 * @date 2022-07-21
 * @since 1.0
 */
public class TraceFilter implements GlobalFilter, Ordered {

    private final Tracer tracer;

    public TraceFilter(Tracer tracer) {
        this.tracer = tracer;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Span span = tracer.currentSpan();
        if (span == null) {
            span = tracer.nextSpan();
        }

        TraceContext context = span.context();
        ServerHttpResponse response = exchange.getResponse();
        HttpHeaders headers = response.getHeaders();
        // 设置链路信息到响应头
        headers.put(SleuthConstant.SLEUTH_TRACE_ID, List.of(context.traceId()));
        headers.put(SleuthConstant.SLEUTH_SPAN_ID, List.of(context.spanId()));
        String parentId = context.parentId();
        headers.put(SleuthConstant.SLEUTH_PARENT_SPAN_ID, List.of(parentId == null ? "" : parentId));
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return  Ordered.HIGHEST_PRECEDENCE;
    }

}
