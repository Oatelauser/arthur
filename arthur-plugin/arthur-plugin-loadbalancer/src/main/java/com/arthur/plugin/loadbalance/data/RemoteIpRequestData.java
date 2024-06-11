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
 * 远程IP负载均衡请求数据载体
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-01-15
 * @since 1.0
 */
public class RemoteIpRequestData extends RequestData {

	/**
	 * 访问的远程IP地址
	 */
	private String remoteIp;

	public RemoteIpRequestData(HttpMethod httpMethod, URI url, HttpHeaders headers, MultiValueMap<String, String> cookies, Map<String, Object> attributes) {
		super(httpMethod, url, headers, cookies, attributes);
	}

	public RemoteIpRequestData(ClientRequest request) {
		super(request);
	}

	public RemoteIpRequestData(HttpRequest request) {
		super(request);
	}

	public RemoteIpRequestData(ServerHttpRequest request) {
		super(request, Map.of());
	}

	public RemoteIpRequestData(ServerHttpRequest request, Map<String, Object> attributes) {
		super(request, attributes);
	}

	public String getRemoteIp() {
		return remoteIp;
	}

	public void setRemoteIp(String remoteIp) {
		this.remoteIp = remoteIp;
	}

}
