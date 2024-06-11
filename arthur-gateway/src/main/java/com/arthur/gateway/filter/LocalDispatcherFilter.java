/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.arthur.gateway.filter;

import com.arthur.boot.core.url.UrlPathMather;
import com.arthur.plugin.constant.Constant;
import com.arthur.plugin.util.LocalEncryption;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.reactive.DispatcherHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static com.arthur.plugin.constant.Constant.LOCAL_DISPATCHER_PATH;

/**
 * The type Local dispatcher filter.
 */
public class LocalDispatcherFilter implements WebFilter {

    private final String localSecretKey;
    private final UrlPathMather urlPathMather;
    private final DispatcherHandler dispatcherHandler;

    /**
     * Instantiates a new Local dispatcher filter.
     *
     * @param dispatcherHandler the dispatcher handler
     * @param localSecretKey    local request
     */
    public LocalDispatcherFilter(final String localSecretKey, final DispatcherHandler dispatcherHandler, final UrlPathMather urlPathMather) {
        this.localSecretKey = localSecretKey;
        this.urlPathMather = urlPathMather;
        this.dispatcherHandler = dispatcherHandler;
    }

    /**
     * Process the Web request and (optionally) delegate to the next
     * {@code WebFilter} through the given {@link WebFilterChain}.
     *
     * @param exchange the current server exchange
     * @param chain    provides a way to delegate to the next filter
     * @return {@code Mono<Void>} to indicate when request processing is complete
     */
    @Override
    @NonNull
    public Mono<Void> filter(@NonNull final ServerWebExchange exchange, @NonNull final WebFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        // 如果是内部请求，并且私钥一样，则直接不走下面的过滤器，直接放行
        if (urlPathMather.matches(LOCAL_DISPATCHER_PATH, path)) {
            String clientSecretKey = exchange.getRequest().getHeaders().getFirst(Constant.LOCAL_KEY);
            if (Objects.isNull(localSecretKey) || Objects.isNull(clientSecretKey) || !localSecretKey.equals(LocalEncryption.encrypt(clientSecretKey))) {
                return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "The key is not correct."));
            }
            return dispatcherHandler.handle(exchange);
        }
        return chain.filter(exchange);
    }

}
