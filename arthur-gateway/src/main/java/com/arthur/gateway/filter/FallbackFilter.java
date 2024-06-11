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
import com.arthur.common.utils.StringUtils;
import com.arthur.plugin.result.ArthurResult;
import com.arthur.plugin.result.ArthurStatusEnum;
import com.arthur.plugin.util.WebFluxResultUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import static com.arthur.plugin.constant.Constant.FALLBACK_SENTINEL_URL;

/**
 * FallbackFilter.
 */
public class FallbackFilter implements WebFilter {

    private static final Logger LOG = LoggerFactory.getLogger(FallbackFilter.class);

    private final String fallbackPath;
    private final UrlPathMather urlPathMather;

    public FallbackFilter(final String path, UrlPathMather urlPathMather) {
        this.fallbackPath = StringUtils.isBlank(path) ? FALLBACK_SENTINEL_URL : path;
        this.urlPathMather = urlPathMather;
    }

    @Override
    @NonNull
    public Mono<Void> filter(@NonNull final ServerWebExchange exchange, @NonNull final WebFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        if (urlPathMather.matches(fallbackPath, path)) {
            Object error = this.getError(exchange);
            return WebFluxResultUtils.result(exchange, error);
        }

        return chain.filter(exchange);
    }

    private Object getError(final ServerWebExchange exchange) {
        return ArthurResult.ofError(exchange, ArthurStatusEnum.FALLBACK_DEFAULT, null);
    }

}
