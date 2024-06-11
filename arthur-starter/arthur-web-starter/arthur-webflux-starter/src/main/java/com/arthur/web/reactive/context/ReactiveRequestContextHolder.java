package com.arthur.web.reactive.context;

import org.springframework.http.server.reactive.ServerHttpRequest;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.function.Function;

/**
 * 获取当前请求对象{@link ServerHttpRequest}
 * <p>
 * 参考实现{@code org.springframework.security.core.context.ReactiveSecurityContextHolder}
 *
 * @author DearYang
 * @date 2022-10-15
 * @since 1.0
 */
public abstract class ReactiveRequestContextHolder {

	static final Class<ServerHttpRequest> REQUEST_CONTEXT_KEY = ServerHttpRequest.class;

	static Context withRequestContext(Mono<ServerHttpRequest> requestContext) {
		return Context.of(REQUEST_CONTEXT_KEY, requestContext);
	}

	/**
	 * 获取当前请求对象
	 */
	public static Mono<ServerHttpRequest> getContext() {
		return Mono.deferContextual(Mono::just)
			.cast(Context.class)
			.filter(ReactiveRequestContextHolder::hasRequestContext)
			.flatMap(ReactiveRequestContextHolder::getRequestContext);
	}

	/**
	 * 清空上下文
	 *
	 * @see Mono#contextWrite(Function)
	 */
	public static Function<Context, Context> clearContext() {
		return context -> context.delete(REQUEST_CONTEXT_KEY);
	}

	private static boolean hasRequestContext(Context context) {
		return context.hasKey(REQUEST_CONTEXT_KEY);
	}

	private static Mono<ServerHttpRequest> getRequestContext(Context context) {
		return context.<Mono<ServerHttpRequest>>get(REQUEST_CONTEXT_KEY);
	}

}
