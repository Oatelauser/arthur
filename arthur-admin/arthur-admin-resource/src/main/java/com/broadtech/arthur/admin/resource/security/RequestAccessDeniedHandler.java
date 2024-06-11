package com.broadtech.arthur.admin.resource.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/9/15
 */
@Component
public class RequestAccessDeniedHandler extends AccessHandler implements ServerAccessDeniedHandler {
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {
        return super.handler(exchange,denied);
    }


}
