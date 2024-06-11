package com.arthur.web.servlet.version;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.lang.NonNull;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.condition.AbstractRequestCondition;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * 基于请求版本构建的请求条件
 *
 * @author DearYang
 * @date 2022-10-12
 * @since 1.0
 */
public class RequestVersionRequestCondition extends AbstractRequestCondition<RequestVersionRequestCondition> {

	private final String version;
	private final String[] urls;
	private final RequestMappingVersion requestMappingVersion;
	private final AntPathMatcher antPathMatcher = new AntPathMatcher();

	public RequestVersionRequestCondition(String version, String[] urls, RequestMappingVersion requestMappingVersion) {
		this.urls = urls;
		this.version = version;
		this.requestMappingVersion = requestMappingVersion;
	}

	@NonNull
	@Override
	public RequestVersionRequestCondition combine(@NonNull RequestVersionRequestCondition other) {
		if (isEmpty() && other.isEmpty()) {
			return this;
		} else if (other.isEmpty()) {
			return this;
		} else if (isEmpty()) {
			return other;
		}

		return new RequestVersionRequestCondition(other.version, other.urls, other.requestMappingVersion);
	}

	@Override
	public RequestVersionRequestCondition getMatchingCondition(@NonNull HttpServletRequest request) {
		for (String url : urls) {
			if (this.matches(url, request.getRequestURI())) {
				return this;
			}
		}
		return null;
	}

	@NonNull
	@Override
	protected Collection<Integer> getContent() {
		int apiVersion = this.requestMappingVersion.version();
		return apiVersion < 0 ? Collections.emptyList() : Collections.singletonList(apiVersion);
	}

	@NonNull
	@Override
	protected String getToStringInfix() {
		return "||";
	}

	@Override
	public int compareTo(RequestVersionRequestCondition condition, @NonNull HttpServletRequest request) {
		return condition.requestMappingVersion.version() - this.requestMappingVersion.version();
	}

	private boolean matches(String pattern, String path) {
		Map<String, String> uriVariables = antPathMatcher.extractUriTemplateVariables(pattern, path);
		String versionVariable = uriVariables.get(this.version);
		if (!StringUtils.hasText(versionVariable)) {
			return false;
		}

		String prefixVersion = this.requestMappingVersion.prefix();
		int index = versionVariable.indexOf(prefixVersion);
		if (index < 0) {
			index = 0;
		} else {
			index += prefixVersion.length();
		}

		int srcVersion = Integer.parseInt(versionVariable.substring(index));
		int sourceVersion = this.requestMappingVersion.version();
		return switch (this.requestMappingVersion.conditionType()) {
			case EQUAL -> srcVersion == sourceVersion;
			case GT -> srcVersion > sourceVersion;
			case GTE -> srcVersion >= sourceVersion;
			case LT -> srcVersion < sourceVersion;
			case LTE -> srcVersion <= sourceVersion;
		};
	}

}
