package com.arthur.oauth2.feign;

import com.arthur.oauth2.utils.WebUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Optional;

import static com.arthur.auth.user.constant.InternalAuthConstants.INTERNAL_AUTH_HEADER_NAME;
import static com.arthur.auth.user.constant.InternalAuthConstants.INTERNAL_AUTH_HEADER_VALUE;

/**
 * OAuth2 OpenFeign 令牌传递
 *
 * @author DearYang
 * @date 2022-08-14
 * @since 1.0
 */
public class OAuth2TokenRequestInterceptor implements RequestInterceptor {

	private final BearerTokenResolver tokenResolver;

	public OAuth2TokenRequestInterceptor(BearerTokenResolver tokenResolver) {
		this.tokenResolver = tokenResolver;
	}

	@Override
	public void apply(RequestTemplate template) {
		Collection<String> authHeader = template.headers().get(INTERNAL_AUTH_HEADER_NAME);
		if (!CollectionUtils.isEmpty(authHeader) && authHeader.contains(INTERNAL_AUTH_HEADER_VALUE)) {
			return;
		}

		Optional<HttpServletRequest> optional = WebUtils.getRequest();
		if (optional.isEmpty()) {
			return;
		}

		HttpServletRequest request = optional.get();
		addRequestHeaders(request, template);

		String token = tokenResolver.resolve(request);
		if (StringUtils.hasText(token)) {
			template.header(HttpHeaders.AUTHORIZATION, TokenType.BEARER + " " + token);
		}
	}

	private void addRequestHeaders(HttpServletRequest request, RequestTemplate template) {
		Enumeration<String> headerNames = request.getHeaderNames();
		if (headerNames == null) {
			return;
		}

		while (headerNames.hasMoreElements()) {
			String name = headerNames.nextElement();
			// 跳过content-length值的复制。因为服务之间调用需要携带一些用户信息之类的 所以实现了Feign的RequestInterceptor拦截器复制请求头，复制的时候是所有头都复制的,可能导致Content-length长度跟body不一致
			// @see https://blog.csdn.net/qq_39986681/article/details/107138740
			if (name.equalsIgnoreCase(HttpHeaders.CONTENT_LENGTH)) {
				continue;
			}

			String value = request.getHeader(name);
			// 解决 UserAgent 信息被修改后，AppleWebKit/537.36 (KHTML,like Gecko)部分存在非法字符的问题
			if (name.equalsIgnoreCase(HttpHeaders.USER_AGENT)) {
				value = value.replace("\n", "");
			}

			template.header(name, value);
		}
	}

}
