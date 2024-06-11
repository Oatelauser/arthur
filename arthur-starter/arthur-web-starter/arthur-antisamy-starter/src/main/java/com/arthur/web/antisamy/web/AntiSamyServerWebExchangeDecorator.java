package com.arthur.web.antisamy.web;

import com.arthur.web.antisamy.context.AntiSamyService;
import com.arthur.web.reactive.codec.FormFieldPartDecorator;
import com.arthur.web.reactive.utils.WebfluxUtils;
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.lang.NonNull;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebExchangeDecorator;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * 表单请求、和Multipart请求内容修改
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-13
 * @since 1.0
 */
public class AntiSamyServerWebExchangeDecorator extends ServerWebExchangeDecorator {

	private final AntiSamyService antiSamyService;
	private final Mono<MultiValueMap<String, String>> formDataMono;
	private final Mono<MultiValueMap<String, Part>> multipartDataMono;


	protected AntiSamyServerWebExchangeDecorator(ServerWebExchange delegate, AntiSamyService antiSamyService) {
		super(delegate);
		this.antiSamyService = antiSamyService;
		this.formDataMono = delegate.getFormData().map(this::initFormData).cache();
		this.multipartDataMono = delegate.getMultipartData().map(this::initMultipartData).cache();
	}

	@NonNull
	@Override
	public Mono<MultiValueMap<String, String>> getFormData() {
		return formDataMono;
	}

	@NonNull
	@Override
	public Mono<MultiValueMap<String, Part>> getMultipartData() {
		return multipartDataMono;
	}

	private MultiValueMap<String, String> initFormData(MultiValueMap<String, String> data) {
		if (CollectionUtils.isEmpty(data) || !antiSamyService.config().isDefenseQueryBody()) {
			return data;
		}

		for (Map.Entry<String, List<String>> entry : data.entrySet()) {
			List<String> value = entry.getValue();
			if (!CollectionUtils.isEmpty(value)) {
				value.replaceAll(antiSamyService::cleaning);
			}
		}

		return data;
	}

	private MultiValueMap<String, Part> initMultipartData(MultiValueMap<String, Part> data) {
		if (CollectionUtils.isEmpty(data) || !antiSamyService.config().isDefenseQueryBody()) {
			return data;
		}

		for (Map.Entry<String, List<Part>> entry : data.entrySet()) {
			List<Part> parts = entry.getValue();
			if (CollectionUtils.isEmpty(parts)) {
				continue;
			}

			for (int i = 0; i < parts.size(); i++) {
				Part part = this.resolveMultipartData(parts.get(i));
				if (part != null) {
					parts.set(i, part);
				}
			}
		}

		return data;
	}

	private Part resolveMultipartData(Part part) {
		if (WebfluxUtils.judgeMultipartFile(part)
			&& part instanceof FormFieldPart fieldPart) {
			String fieldValue = antiSamyService.cleaning(fieldPart.value());
			return new FormFieldPartDecorator(fieldValue, fieldPart);
		}
		return null;
	}

}
