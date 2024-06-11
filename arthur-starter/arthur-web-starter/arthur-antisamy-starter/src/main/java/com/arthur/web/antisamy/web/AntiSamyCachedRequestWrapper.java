package com.arthur.web.antisamy.web;

import com.arthur.web.antisamy.context.AntiSamyService;
import com.arthur.web.servlet.web.ContentCachedRequestWrapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Map;
import java.util.Objects;

/**
 * Servlet AntiSamy XSS 实现 {@link HttpServletRequestWrapper}
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-06
 * @since 1.0
 */
public class AntiSamyCachedRequestWrapper extends ContentCachedRequestWrapper {

	private final AntiSamyService antiSamyService;

	public AntiSamyCachedRequestWrapper(HttpServletRequest request, AntiSamyService antiSamyService) {
		super(request);
		this.antiSamyService = Objects.requireNonNull(antiSamyService, "antiSamyService");
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		Map<String, String[]> parameterMap = super.getParameterMap();
		if (CollectionUtils.isEmpty(parameterMap)) {
			return parameterMap;
		}

		boolean defenseQueryBody = antiSamyService.config().isDefenseQueryBody();
		for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
			String[] values = entry.getValue();
			for (int i = 0; i < values.length; i++) {
				String value = values[i];
				if (StringUtils.hasText(value) && defenseQueryBody) {
					values[i] = antiSamyService.cleaning(value);
				}
			}
		}
		return parameterMap;
	}

	@Override
	public String[] getParameterValues(String name) {
		String[] values = super.getParameterValues(name);
		if (ObjectUtils.isEmpty(values)) {
			return values;
		}

		boolean defenseQueryBody = antiSamyService.config().isDefenseQueryBody();
		int size = values.length;
		for (int i = 0; i < size; i++) {
			String value = values[i];
			if (StringUtils.hasText(value) && defenseQueryBody) {
				values[i] = antiSamyService.cleaning(value);
			}
		}

		return values;
	}

	@Override
	public String getParameter(String name) {
		String value = super.getParameter(name);
		if (!StringUtils.hasText(value)) {
			return value;
		}

		if (antiSamyService.config().isDefenseQueryBody()) {
			value = antiSamyService.cleaning(value);
		}
		return value;
	}

	@Override
	public Enumeration<String> getHeaders(String name) {
		Enumeration<String> headers = super.getHeaders(name);
		if (headers == null || !headers.hasMoreElements() || ObjectUtils.isEmpty(antiSamyService.config().getDefenseHeaders())) {
			return headers;
		}

		if (antiSamyService.hasXssHeader(name)) {
			headers = new AntiSamyHeader(headers, antiSamyService);
		}
		return headers;
	}

	@Override
	public String getHeader(String name) {
		String header = super.getHeader(name);
		if (!StringUtils.hasText(header) || ObjectUtils.isEmpty(antiSamyService.config().getDefenseHeaders())) {
			return header;
		}

		if (antiSamyService.hasXssHeader(name)) {
			header = antiSamyService.cleaning(header);
		}
		return header;
	}

	@Override
	public Cookie[] getCookies() {
		Cookie[] cookies = super.getCookies();
		if (ObjectUtils.isEmpty(cookies) || ObjectUtils.isEmpty(antiSamyService.config().getDefenseCookies())) {
			return cookies;
		}

		for (Cookie cookie : cookies) {
			String cookieName = cookie.getName();
			String cookieValue = cookie.getValue();
			if (StringUtils.hasText(cookieValue) && antiSamyService.hasXssHeader(cookieName)) {
				cookieValue = antiSamyService.cleaning(cookieValue);
			}
			cookie.setValue(cookieValue);
		}
		return cookies;
	}

	/**
	 * 防御application/json请求
	 */
	@Override
	protected byte[] handleRequestContent(byte[] cachedContent) throws UnsupportedEncodingException {
		String character = this.getCharacterEncoding();
		String content = new String(cachedContent, 0, getContentLength(), character);
		content = antiSamyService.cleaning(content);
		return content.getBytes(character);
	}

}
