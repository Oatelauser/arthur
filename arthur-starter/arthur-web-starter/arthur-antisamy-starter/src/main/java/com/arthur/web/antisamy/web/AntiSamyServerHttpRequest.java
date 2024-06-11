package com.arthur.web.antisamy.web;

import com.arthur.web.antisamy.context.AntiSamyService;
import com.arthur.web.reactive.support.CachedBodyHttpOutputMessage;
import com.arthur.web.reactive.support.ModifyServerHttpRequest;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.lang.NonNull;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Webflux AntiSamy XSS 实现 {@link ServerHttpRequestDecorator}
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-06
 * @since 1.0
 */
public class AntiSamyServerHttpRequest extends ModifyServerHttpRequest {

	private final AntiSamyService antiSamyService;

	public AntiSamyServerHttpRequest(HttpHeaders headers, CachedBodyHttpOutputMessage outputMessage,
			ServerHttpRequest delegate, AntiSamyService antiSamyService) {
		super(headers, delegate, outputMessage);
		this.antiSamyService = Objects.requireNonNull(antiSamyService, "antiSamyService");
	}

	@NonNull
	@Override
	public HttpHeaders getHeaders() {
		HttpHeaders headers = super.getHeaders();
		if (ObjectUtils.isEmpty(antiSamyService.config().getDefenseHeaders()) || headers.isEmpty()) {
			return headers;
		}

		for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
			String name = entry.getKey();
			List<String> values = entry.getValue();
			for (int i = 0; i < values.size(); i++) {
				String value = values.get(i);
				if (StringUtils.hasText(value) && antiSamyService.hasXssHeader(name)) {
					values.set(i, antiSamyService.cleaning(value));
				}
			}
		}
		return headers;
	}

	@NonNull
	@Override
	public MultiValueMap<String, HttpCookie> getCookies() {
		MultiValueMap<String, HttpCookie> cookies = super.getCookies();
		if (ObjectUtils.isEmpty(antiSamyService.config().getDefenseCookies()) || cookies.isEmpty()) {
			return cookies;
		}

		for (Map.Entry<String, List<HttpCookie>> entry : cookies.entrySet()) {
			List<HttpCookie> values = entry.getValue();
			for (int i = 0; i < values.size(); i++) {
				HttpCookie cookie = values.get(i);
				String name = cookie.getName();
				String value = cookie.getValue();
				if (StringUtils.hasText(value) && antiSamyService.hasXssCookie(name)) {
					values.set(i, new HttpCookie(name, antiSamyService.cleaning(value)));
				}
			}
		}
		return cookies;
	}

}
