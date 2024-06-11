package com.arthur.web.reactive.context;

import org.springframework.core.Ordered;
import org.springframework.lang.NonNull;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import static com.arthur.web.reactive.context.ReactiveRequestContextHolder.REQUEST_CONTEXT_KEY;

/**
 * Webflux 请求上下文过滤器，实现类似{@link ThreadLocal}的方式保存当前Webflux的请求对象{@link org.springframework.http.server.ServerHttpRequest}
 * <p>
 * 参考实现{@code org.springframework.security.web.server.context.ReactorContextWebFilter}
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-06
 * @see ReactiveRequestContextHolder
 * @since 1.0
 */
public class ReactorContextWebFilter implements WebFilter, Ordered {

	@NonNull
	@Override
	public Mono<Void> filter(@NonNull ServerWebExchange exchange, WebFilterChain chain) {
		return chain.filter(exchange).contextWrite(context ->
			context.hasKey(REQUEST_CONTEXT_KEY) ? context : withRequestContext(context, exchange));
	}

	private Context withRequestContext(Context mainContext, ServerWebExchange exchange) {
		return mainContext.putAll(Mono.just(exchange.getRequest())
			.as(ReactiveRequestContextHolder::withRequestContext).readOnly());
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}

}
