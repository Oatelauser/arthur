package com.arthur.web.reactive.server;

import com.arthur.common.utils.ClassUtils;
import com.arthur.web.model.GenericServerResponse;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.server.EntityResponse;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.arthur.web.reactive.constant.WebfluxConstants.ORIGINAL_RESPONSE_CONTENT_TYPE_ATTR;

/**
 * Webflux写入响应内容
 * <p>
 * 用于{@link org.springframework.web.server.WebFilter}等过滤器中返回自定义响应内容
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-20
 * @since 1.0
 */
@SuppressWarnings("rawtypes")
public class WebServerResponse {

	private final ServerResponse.Context serverResponseContext;

	public WebServerResponse(ExchangeStrategies strategies,
			List<ViewResolver> viewResolvers) {
		this.serverResponseContext = new ServerResponseContext(strategies, viewResolvers);
	}

	/**
	 * 写入响应内容并返回
	 *
	 * @param response {@link ServerResponse}
	 * @param exchange {@link ServerWebExchange}
	 */
	public Mono<Void> writeTo(ServerResponse response, ServerWebExchange exchange) {
		if (exchange.getResponse().isCommitted()) {
			return Mono.empty();
		}
		return response.writeTo(exchange, serverResponseContext);
	}

	/**
	 * @see #writeTo(ServerResponse, ServerWebExchange)
	 */
	public Mono<Void> writeTo(HttpStatus status, GenericServerResponse serverResponse,
			ServerWebExchange exchange) {
		return EntityResponse.fromObject(serverResponse)
			.status(status)
			.build()
			.map(response -> response)
			.flatMap(response -> this.writeTo(response, exchange));
	}

	/**
	 * @see #writeTo(HttpStatus, GenericServerResponse, ServerWebExchange)
	 */
	public Mono<Void> writeTo(GenericServerResponse serverResponse, ServerWebExchange exchange) {
		return this.writeTo(HttpStatus.OK, serverResponse, exchange);
	}

	/**
	 * 基于Reactive的{@link GenericServerResponse}对象响应结果
	 *
	 * @param status         响应码
	 * @param serverResponse {@link Publisher}
	 * @param exchange       {@link ServerWebExchange}
	 */
	public Mono<Void> writeTo(HttpStatus status, Publisher<GenericServerResponse> serverResponse,
			ServerWebExchange exchange) {
		return EntityResponse.fromPublisher(serverResponse, GenericServerResponse.class)
			.status(status)
			.build()
			.map(response -> response)
			.flatMap(response -> this.writeTo(response, exchange));
	}

	/**
	 * @see #writeTo(HttpStatus, Publisher, ServerWebExchange)
	 */
	public Mono<Void> writeTo(Publisher<GenericServerResponse> serverResponse, ServerWebExchange exchange) {
		return writeTo(HttpStatus.OK, serverResponse, exchange);
	}

	/**
	 * 指定响应内容对象响应
	 *
	 * @param status 响应码
	 * @param data 响应内容
	 * @param exchange {@link ServerWebExchange}
	 */
	public Mono<Void> writeTo(HttpStatus status, Object data, ServerWebExchange exchange) {
		Class<?> dataType = data.getClass();
		EntityResponse.Builder<Object> objectBuilder = EntityResponse.fromObject(data);
		if (ClassUtils.isPrimitiveWrapper(dataType) ||
				data instanceof CharSequence || data  instanceof byte[]) {
			objectBuilder = objectBuilder.contentType(this.resolveContentType(exchange));
		}

		return objectBuilder.status(status).build().map(response -> response)
			.flatMap(response -> this.writeTo(response, exchange));
	}

	/**
	 * @see #writeTo(HttpStatus, Object, ServerWebExchange)
	 */
	public Mono<Void> writeTo(Object data, ServerWebExchange exchange) {
		return this.writeTo(HttpStatus.OK, data, exchange);
	}

	/**
	 * 解析ContentType
	 */
	private MediaType resolveContentType(ServerWebExchange exchange) {
		String contentType = exchange.getAttribute(ORIGINAL_RESPONSE_CONTENT_TYPE_ATTR);
		if (StringUtils.hasText(contentType)) {
			return MediaType.valueOf(contentType);
		}
		return MediaType.APPLICATION_JSON;
	}

}
