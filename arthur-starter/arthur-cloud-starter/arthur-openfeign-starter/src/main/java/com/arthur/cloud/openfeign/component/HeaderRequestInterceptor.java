package com.arthur.cloud.openfeign.component;

import com.arthur.cloud.openfeign.utils.WebUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;

import java.util.Enumeration;
import java.util.Optional;

/**
 * Feign拦截器
 * <p>
 * 拷贝请求头防止数据丢失
 *
 * @author DearYang
 * @date 2022-09-06
 * @since 1.0
 */
public class HeaderRequestInterceptor implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate template) {
		Optional<HttpServletRequest> optional = WebUtils.getRequest();
		if (optional.isEmpty()) {
			return;
		}

		HttpServletRequest request = optional.get();
		addRequestHeaders(request, template);
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
