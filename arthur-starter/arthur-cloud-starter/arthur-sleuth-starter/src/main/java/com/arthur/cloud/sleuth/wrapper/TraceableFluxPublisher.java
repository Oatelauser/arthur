package com.arthur.cloud.sleuth.wrapper;

import org.springframework.cloud.sleuth.CurrentTraceContext;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.lang.NonNull;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Flux;

/**
 * proxy Flux
 *
 * @author DearYang
 * @date 2022-07-21
 * @since 1.0
 */
public class TraceableFluxPublisher<T> extends Flux<T> {

    private final Span span;
    private final Tracer tracer;
    private final Flux<T> delegate;
    private final CurrentTraceContext currentTraceContext;

    public TraceableFluxPublisher(Flux<T> delegate, Span span, Tracer tracer, CurrentTraceContext currentTraceContext) {
        this.span = span;
        this.tracer = tracer;
        this.delegate = delegate;
        this.currentTraceContext = currentTraceContext;
    }

    @Override
    public void subscribe(@NonNull CoreSubscriber<? super T> actual) {
        delegate.subscribe(new TraceableCoreSubscriber<>(actual, span, tracer, currentTraceContext));
    }

}
