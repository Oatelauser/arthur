package com.broadtech.arthur.admin.resource.security;

import com.alibaba.fastjson2.JSON;
import com.broadtech.arthur.admin.resource.response.ResponseVo;
import com.broadtech.arthur.admin.resource.response.ReturnCode;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/9/15
 */
public abstract class AccessHandler {
    public Mono<Void> handler(ServerWebExchange exchange, Exception ex){
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        String body = JSON.toJSONString(new ResponseVo<>(ex.getMessage(), ReturnCode.RC204, ReturnCode.RC204.toString()));
        DataBuffer bodyWrap = response.bufferFactory().wrap(body.getBytes());
        return response.writeWith(Mono.just(bodyWrap));
    }
}
