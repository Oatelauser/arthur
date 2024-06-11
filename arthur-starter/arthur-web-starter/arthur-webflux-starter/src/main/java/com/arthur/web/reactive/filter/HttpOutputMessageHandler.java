package com.arthur.web.reactive.filter;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 请求报文处理器
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-10
 * @see ModifyRequestBodyWebFilter
 * @since 1.0
 */
public interface HttpOutputMessageHandler<I> {

    /**
     * 处理请求报文
     *
     * @param payload  报文
     * @param exchange {@link ServerWebExchange}
     * @return 处理后的请求报文
     */
    Mono<I> handleHttpMessage(I payload, ServerWebExchange exchange);

}
