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
import com.arthur.common.utils.ConcurrentHashSet;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Set;

/**
 * exclude url filter.
 */
public class ExcludeFilter implements WebFilter {

    private final UrlPathMather urlPathMather;
    private final Set<String> paths;

    /**
     * Instantiates a new Exclude filter.
     *
     * @param paths the paths
     */
    public ExcludeFilter(final Set<String> paths, UrlPathMather urlPathMather) {
        this.paths = new ConcurrentHashSet<>(paths);
        this.urlPathMather = urlPathMather;
    }

    @Override
    @NonNull
    public Mono<Void> filter(@NonNull final ServerWebExchange exchange, @NonNull final WebFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        boolean match = paths.stream().anyMatch(url -> urlPathMather.matches(url, path));
        if (match) {
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.OK);
            return Mono.empty();
        }
        return chain.filter(exchange);
    }

}
