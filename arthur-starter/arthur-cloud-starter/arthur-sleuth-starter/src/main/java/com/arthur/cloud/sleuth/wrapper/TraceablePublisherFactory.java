package com.arthur.cloud.sleuth.wrapper;

import org.springframework.cloud.sleuth.CurrentTraceContext;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * reactor publisher wrapper factory
 * <p>
 * {@link TraceablePublisherFactory#TRACE_REQUEST_ATTR} attribute is setting by {@code TraceWebFilter }
 *
 * @author DearYang
 * @date 2022-07-21
 * @since 1.0
 */
public class TraceablePublisherFactory {

    protected static final String TRACE_REQUEST_ATTR = Span.class.getName();

    private final Tracer tracer;
    private final CurrentTraceContext currentTraceContext;

    public TraceablePublisherFactory(Tracer tracer, CurrentTraceContext currentTraceContext) {
        this.tracer = tracer;
        this.currentTraceContext = currentTraceContext;
    }

    public <T> Flux<T> getTracedFlux(Flux<T> publisher, ServerWebExchange exchange) {
        return new TraceableFluxPublisher<>(publisher, exchange.getAttribute(TRACE_REQUEST_ATTR), tracer, currentTraceContext);
    }

    public <T> Mono<T> getTracedMono(Mono<T> publisher, ServerWebExchange exchange) {
        return new TraceableMonoPublisher<>(publisher, exchange.getAttribute(TRACE_REQUEST_ATTR), tracer, currentTraceContext);
    }

}
