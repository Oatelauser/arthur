package com.broadtech.arthur.admin.resource.filter;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.nimbusds.jose.JWSObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/9/15
 */
@Component
@Slf4j
public class AuthGatewayFilter implements GlobalFilter, Ordered {

    private final String AUTH_FLAG = "Authorization";
    private final String TOKEN_TYPE = "Bearer ";





    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return extractPermissions(exchange, chain);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    private Mono<Void> extractPermissions(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = exchange.getRequest().getHeaders().getFirst(AUTH_FLAG);
        if (StrUtil.isEmpty(token)) {
            return chain.filter(exchange);
        }
        return chain.filter(parseToken(exchange, token));
    }

    private ServerWebExchange parseToken(ServerWebExchange exchange, String token) {
        try {
            //从token中解析用户信息并设置到Header中去
            String realToken = token.replace(TOKEN_TYPE, "");
            JWSObject jwsObject = JWSObject.parse(realToken);
            String payLoadInfo = jwsObject.getPayload().toString();
            log.info("authGlobalFilter payLoad:{}", payLoadInfo);
            return exchange.mutate()
                    .request(parsePayLoad(exchange, payLoadInfo))
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private ServerHttpRequest parsePayLoad(ServerWebExchange exchange, String payLoad) {
        ServerHttpRequest.Builder header = exchange.getRequest().mutate();
        JSONObject payLoadObj = JSON.parseObject(payLoad);
        String user = "id";
        header.header(user, payLoadObj.getString("id"));
        String authorities = "authorities";
        header.header(authorities, payLoadObj.getString(authorities));
        return header.build();
    }

}
