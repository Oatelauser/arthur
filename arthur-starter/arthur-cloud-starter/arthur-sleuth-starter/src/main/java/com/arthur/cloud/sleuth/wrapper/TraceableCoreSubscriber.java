package com.arthur.cloud.sleuth.wrapper;


import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.cloud.sleuth.CurrentTraceContext;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;

/**
 * 自定义实现{@link Subscriber}，检查当前上下文是否存在链路信息
 *
 * @author DearYang
 * @date 2022-07-21
 * @since 1.0
 */
public class TraceableCoreSubscriber<T> implements Subscriber<T> {

    private final Span span;
    private final Tracer tracer;
    private final Subscriber<T> delegate;
    private final CurrentTraceContext currentTraceContext;

    public TraceableCoreSubscriber(Subscriber<T> delegate, Span span, Tracer tracer, CurrentTraceContext currentTraceContext) {
        this.span = span;
        this.tracer = tracer;
        this.delegate = delegate;
        this.currentTraceContext = currentTraceContext;
    }

    @Override
    public void onSubscribe(Subscription s) {
        if (tracer.currentSpan() != null) {
            delegate.onSubscribe(s);
            return;
        }

        try (CurrentTraceContext.Scope ignored = currentTraceContext.maybeScope(span.context())) {
            delegate.onSubscribe(s);
        }
    }

    @Override
    public void onNext(T t) {
        if (tracer.currentSpan() != null) {
            delegate.onNext(t);
            return;
        }

        try (CurrentTraceContext.Scope ignored = currentTraceContext.maybeScope(span.context())) {
            delegate.onNext(t);
        }
    }

    @Override
    public void onError(Throwable t) {
        if (tracer.currentSpan() != null) {
            delegate.onError(t);
            return;
        }

        try (CurrentTraceContext.Scope ignored = currentTraceContext.maybeScope(span.context())) {
            delegate.onError(t);
        }
    }

    @Override
    public void onComplete() {
        if (tracer.currentSpan() != null) {
            delegate.onComplete();
            return;
        }

        try (CurrentTraceContext.Scope ignored = currentTraceContext.maybeScope(span.context())) {
            delegate.onComplete();
        }
    }

}
