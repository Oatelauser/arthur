package com.arthur.web.reactive.filter;

import com.arthur.web.reactive.support.BodyInserterContext;
import com.arthur.web.reactive.support.CachedBodyHttpOutputMessage;
import com.arthur.web.reactive.support.ModifyServerHttpRequest;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.lang.NonNull;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * 修改请求内容通用过滤器
 * <p>
 * 参考{@code org.springframework.cloud.gateway.filter.factory.rewrite.ModifyRequestBodyGatewayFilterFactory}
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-09
 * @since 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ModifyRequestBodyWebFilter<I> implements WebFilter {

    private String contentType;
    private final Class outClass;
    private final Class<I> inClass;
    private final List<HttpMessageReader<?>> httpMessageReaders;
    private final HttpOutputMessageHandler<I> outputMessageHandler;

    public ModifyRequestBodyWebFilter(Class<I> inClass, Class outClass,
            HttpOutputMessageHandler<I> outputMessageHandler,
            ObjectProvider<List<HttpMessageReader<?>>> provider) {
        this.inClass = Objects.requireNonNull(inClass, "inClass");
        this.outClass = Objects.requireNonNull(outClass, "outClass");
        this.outputMessageHandler = outputMessageHandler;
        this.httpMessageReaders = provider.getIfAvailable(() -> HandlerStrategies.withDefaults()
                .messageReaders());
    }

    @NonNull
    @Override
    public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        ServerRequest serverRequest = ServerRequest.create(exchange, httpMessageReaders);
        Mono<I> modifiedBody = serverRequest.bodyToMono(inClass)
                .flatMap(payload -> outputMessageHandler.handleHttpMessage(payload, exchange))
                .switchIfEmpty(Mono.defer(() -> outputMessageHandler.handleHttpMessage(null, exchange)));

        BodyInserter bodyInserter = BodyInserters.fromPublisher(modifiedBody, outClass);
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(exchange.getRequest().getHeaders());

        headers.remove(HttpHeaders.CONTENT_LENGTH);
        if (contentType != null) {
            headers.set(HttpHeaders.CONTENT_TYPE, contentType);
        }

        CachedBodyHttpOutputMessage outputMessage = new CachedBodyHttpOutputMessage(headers, exchange);
        return bodyInserter.insert(outputMessage, new BodyInserterContext())
                .then(Mono.defer(() -> this.doFilter(headers, outputMessage, exchange, chain)))
                .onErrorResume((Function<Throwable, Mono>) throwable -> release(outputMessage, throwable));
    }

    protected Mono<Void> doFilter(HttpHeaders headers, CachedBodyHttpOutputMessage outputMessage,
            ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = new ModifyServerHttpRequest(headers, exchange.getRequest(), outputMessage);
        return chain.filter(exchange.mutate().request(request).build());
    }

    protected Mono<Void> release(CachedBodyHttpOutputMessage outputMessage, Throwable throwable) {
        if (outputMessage.isCached()) {
            return outputMessage.getBody().map(DataBufferUtils::release)
				.then(Mono.error(throwable));
        }
        return Mono.error(throwable);
    }

    public Class<I> getInClass() {
        return inClass;
    }

    public Class getOutClass() {
        return outClass;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

}
