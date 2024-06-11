package com.arthur.web.reactive.support;

import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.lang.NonNull;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Supplier;

/**
 * 实现可缓存的内容保存到{@link #body}
 * <p>
 * 参考{@code org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage}
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-09
 * @see ReactiveHttpOutputMessage
 * @since 1.0
 */
public class CachedBodyHttpOutputMessage implements ReactiveHttpOutputMessage {

	private boolean cached = false;
	private final HttpHeaders httpHeaders;
	private final DataBufferFactory bufferFactory;
	private Flux<DataBuffer> body = Flux.error(new IllegalStateException("The body is not set."
		+ "Did handling complete with success?"));

	public CachedBodyHttpOutputMessage(HttpHeaders httpHeaders, ServerWebExchange exchange) {
		this.httpHeaders = httpHeaders;
		this.bufferFactory = exchange.getResponse().bufferFactory();
	}

	@NonNull
	@Override
	public DataBufferFactory bufferFactory() {
		return this.bufferFactory;
	}

	@Override
	public void beforeCommit(@NonNull Supplier<? extends Mono<Void>> action) {
	}

	@Override
	public boolean isCommitted() {
		return false;
	}

	@NonNull
	@Override
	public Mono<Void> writeWith(@NonNull Publisher<? extends DataBuffer> body) {
		this.body = Flux.from(body);
		this.cached = true;
		return Mono.empty();
	}

	@NonNull
	@Override
	public Mono<Void> writeAndFlushWith(@NonNull Publisher<? extends Publisher<? extends DataBuffer>> body) {
		return writeWith(Flux.from(body).flatMap(p -> p));
	}

	@NonNull
	@Override
	public Mono<Void> setComplete() {
		return writeWith(Flux.empty());
	}

	@NonNull
	@Override
	public HttpHeaders getHeaders() {
		return this.httpHeaders;
	}

	public boolean isCached() {
		return cached;
	}

	public Flux<DataBuffer> getBody() {
		return body;
	}

}
