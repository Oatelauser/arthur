package com.arthur.plugin.loadbalance.data;

import org.springframework.cloud.client.loadbalancer.RequestData;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientRequest;

import java.net.URI;
import java.util.Map;

/**
 * 分布式负载均衡请求数据
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-15
 * @since 1.0
 */
@SuppressWarnings("unused")
public class NamespaceRequestData extends RequestData {

	/**
	 * 环境
	 */
	private String env;

	/**
	 * 命名空间名称
	 */
	private String namespace;

	public NamespaceRequestData(HttpMethod httpMethod, URI url, HttpHeaders headers,
			MultiValueMap<String, String> cookies, Map<String, Object> attributes) {
		super(httpMethod, url, headers, cookies, attributes);
	}

	public NamespaceRequestData(ClientRequest request) {
		super(request);
	}

	public NamespaceRequestData(HttpRequest request) {
		super(request);
	}

	public NamespaceRequestData(ServerHttpRequest request) {
		super(request);
	}

	public NamespaceRequestData(ServerHttpRequest request, Map<String, Object> attributes) {
		super(request, attributes);
	}

	public String getEnv() {
		return env;
	}

	public void setEnv(String env) {
		this.env = env;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

}
