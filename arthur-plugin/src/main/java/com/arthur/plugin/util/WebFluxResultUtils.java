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

package com.arthur.plugin.util;

import com.arthur.boot.context.ApplicationContextHolder;
import com.arthur.common.response.ServerResponse;
import com.arthur.common.utils.ClassUtils;
import com.arthur.plugin.result.WebFluxResult;
import com.arthur.plugin.result.WebFluxResultFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Objects;


/**
 * The webflux result utils.
 */
@SuppressWarnings("unchecked")
public abstract class WebFluxResultUtils {

    /**
     * result utils log.
     */
    private static final Logger LOG = LoggerFactory.getLogger(WebFluxResultUtils.class);

    private static final WebFluxResult<ServerResponse> WEB_FLUX_RESULT = ApplicationContextHolder.getBean(WebFluxResultFactory.class).newInstance();

    /**
     * Response result.
     *
     * @param exchange the exchange
     * @param result   the result
     * @return the result
     */
    public static Mono<Void> result(final ServerWebExchange exchange, final Object result) {
        if (Objects.isNull(result)) {
            return Mono.empty();
        }

        // TODO:1.根据设置的不同响应内容返回
        Object resultData = WEB_FLUX_RESULT.format(exchange, result);

        // basic data use text/plain
        MediaType mediaType = MediaType.TEXT_PLAIN;
        if (!ClassUtils.isPrimitiveWrapper(result.getClass()) && !(result instanceof CharSequence)) {
            mediaType = WEB_FLUX_RESULT.contentType(exchange, resultData);
        }
        exchange.getResponse().getHeaders().setContentType(mediaType);

        // set response data
        final Object responseData = Objects.requireNonNull(WEB_FLUX_RESULT.result(exchange, resultData));
        final byte[] bytes = (responseData instanceof byte[])
                ? (byte[]) responseData : responseData.toString().getBytes(StandardCharsets.UTF_8);
        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                        .bufferFactory().wrap(bytes))
                .doOnNext(data -> exchange.getResponse().getHeaders().setContentLength(data.readableByteCount())));
    }

    ///**
    // * get no selector result.
    // *
    // * @param pluginName the plugin name
    // * @param exchange   the exchange
    // * @return the mono
    // */
    //public static Mono<Void> noSelectorResult(final String pluginName, final ServerWebExchange exchange) {
    //    LOG.error("can not match selector data: {}", pluginName);
    //    Object error = ShenyuResultWrap.error(exchange, ShenyuResultEnum.SELECTOR_NOT_FOUND.getCode(), pluginName + ":" + ShenyuResultEnum.SELECTOR_NOT_FOUND.getMsg(), null);
    //    return WebFluxResultUtils.result(exchange, error);
    //}
    //
    ///**
    // * get no rule result.
    // *
    // * @param pluginName the plugin name
    // * @param exchange   the exchange
    // * @return the mono
    // */
    //public static Mono<Void> noRuleResult(final String pluginName, final ServerWebExchange exchange) {
    //    LOG.error("can not match rule data: {}", pluginName);
    //    Object error = ShenyuResultWrap.error(exchange, ShenyuResultEnum.RULE_NOT_FOUND.getCode(), pluginName + ":" + ShenyuResultEnum.RULE_NOT_FOUND.getMsg(), null);
    //    return WebFluxResultUtils.result(exchange, error);
    //}
}
