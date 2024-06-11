package com.arthur.web.antisamy.web;

import com.arthur.web.antisamy.context.AntiSamyService;
import com.arthur.web.reactive.filter.HttpOutputMessageHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.arthur.web.antisamy.constant.AntiSamyConstants.ANTISAMY_SERVICE_PARAMETER;

/**
 * 请求内容处理器
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-10
 * @since 1.0
 */
public class AntiSamyHttpOutputMessageHandler implements HttpOutputMessageHandler<String> {

    @Override
    public Mono<String> handleHttpMessage(String payload, ServerWebExchange exchange) {
        return Mono.justOrEmpty((AntiSamyService) exchange.getAttribute(ANTISAMY_SERVICE_PARAMETER))
                .map(antiSamyService -> antiSamyService.cleaning(payload))
                .switchIfEmpty(Mono.just(payload));
    }

}
