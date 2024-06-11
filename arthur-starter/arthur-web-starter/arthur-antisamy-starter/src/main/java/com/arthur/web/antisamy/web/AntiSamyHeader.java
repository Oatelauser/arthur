package com.arthur.web.antisamy.web;

import com.arthur.web.antisamy.context.AntiSamyService;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Enumeration;

/**
 * AntiSamy XSS 防御请求头
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-06
 * @since 1.0
 */
public class AntiSamyHeader implements Enumeration<String> {

	private final AntiSamyService antiSamyService;
	private final Enumeration<String> headers;

	public AntiSamyHeader(Enumeration<String> headers, AntiSamyService antiSamyService) {
		this.headers = headers;
		this.antiSamyService = antiSamyService;
	}

	@Override
	public boolean hasMoreElements() {
		return headers.hasMoreElements();
	}

	@Override
	public String nextElement() {
		String header = headers.nextElement();
		if (!ObjectUtils.isEmpty(antiSamyService.config().getDefenseHeaders()) && StringUtils.hasText(header)) {
			header = antiSamyService.cleaning(header);
		}
		return header;
	}

}
