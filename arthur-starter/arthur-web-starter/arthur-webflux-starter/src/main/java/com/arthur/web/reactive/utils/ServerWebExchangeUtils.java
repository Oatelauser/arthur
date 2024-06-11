package com.arthur.web.reactive.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.accept.RequestedContentTypeResolver;
import org.springframework.web.reactive.accept.RequestedContentTypeResolverBuilder;
import org.springframework.web.server.ServerWebExchange;

import java.net.InetSocketAddress;
import java.util.List;

import static com.arthur.web.constant.RemoteIpConstants.*;
import static com.arthur.web.utils.WebUtils.getMultistageReverseProxyIp;

/**
 * {@link ServerWebExchangeUtils}工具类
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-20
 * @since 1.0
 */
public abstract class ServerWebExchangeUtils {

	private static final RequestedContentTypeResolver CONTENT_TYPE_RESOLVER =
		new RequestedContentTypeResolverBuilder().build();


	/**
	 * @see #resolveRemoteIp(String[], ServerWebExchange)
	 */
	public static String resolveRemoteIp(ServerWebExchange exchange) {
		return resolveRemoteIp(PROXY_IP_HEADERS, exchange);
	}

	/**
	 * 解析请求IP
	 *
	 * @param headers  提取IP的请求头
	 * @param exchange {@link ServerWebExchange}
	 * @return 请求IP
	 */
	public static String resolveRemoteIp(String[] headers, ServerWebExchange exchange) {
		String remoteIp = null;
		HttpHeaders httpHeaders = exchange.getRequest().getHeaders();
		for (String header : headers) {
			remoteIp = httpHeaders.getFirst(header);
			if (StringUtils.hasText(remoteIp) && !UNKNOWN_IP_ADDR.equalsIgnoreCase(remoteIp)) {
				break;
			}
		}

		// 有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
		String ip = getMultistageReverseProxyIp(remoteIp);
		// 还是不能获取到，最后再通过request.getRemoteAddr()获取
		if (!StringUtils.hasText(ip) || UNKNOWN_IP_ADDR.equalsIgnoreCase(remoteIp)) {
			InetSocketAddress remoteAddress = exchange.getRequest().getRemoteAddress();
			if (remoteAddress != null) {
				ip = remoteAddress.getAddress().getHostAddress();
			}
		}
		// 替换localhost
		if (LOCALHOST_MAPPING_IP.equals(ip)) {
			return LOCALHOST_IP;
		}

		return ip == null ? "" : ip;
	}

	/**
	 * 解析媒体类型
	 *
	 * @param exchange {@link ServerWebExchange}
	 * @return 媒体类型
	 */
	public List<MediaType> resolveMediaTypes(ServerWebExchange exchange) {
		List<MediaType> httpRequestMediaTypes = CONTENT_TYPE_RESOLVER.resolveMediaTypes(exchange);
		MimeTypeUtils.sortBySpecificity(httpRequestMediaTypes);
		return httpRequestMediaTypes;
	}

}
