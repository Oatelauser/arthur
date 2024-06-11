package com.broadtech.arthur.admin.resource.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/9/15
 */
@Component
public class RequestAuthenticationEntryPoint extends AccessHandler implements ServerAuthenticationEntryPoint {
    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
        return super.handler(exchange, ex);
    }
}
