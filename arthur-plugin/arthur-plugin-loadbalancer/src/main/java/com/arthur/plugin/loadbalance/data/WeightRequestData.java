package com.arthur.plugin.loadbalance.data;

import org.springframework.cloud.client.loadbalancer.RequestData;
import org.springframework.http.HttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.function.client.ClientRequest;

import java.util.Map;

/**
 * 权重负载均衡请求数据载体
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-01-15
 * @since 1.0
 */
public class WeightRequestData extends RequestData {

	public static final long STARTUP_TIME = Long.MIN_VALUE;
	public static final int DEFAULT_WEIGHT = Integer.MIN_VALUE;
	public static final int DEFAULT_WARMUP = Integer.MIN_VALUE;

	/**
	 * 权重
	 */
	private int weight = DEFAULT_WEIGHT;

	/**
	 * 预热时间，单位毫秒
	 */
	private int warmup = DEFAULT_WARMUP;

	/**
	 * 服务启动时间
	 */
	private long startupTime = STARTUP_TIME;

	public WeightRequestData(ClientRequest request) {
		super(request);
	}

	public WeightRequestData(HttpRequest request) {
		super(request);
	}

	public WeightRequestData(ServerHttpRequest request) {
		this(request, Map.of());
	}

	public WeightRequestData(ServerHttpRequest request, Map<String, Object> attributes) {
		super(request, attributes);
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getWarmup() {
		return warmup;
	}

	public void setWarmup(int warmup) {
		this.warmup = warmup;
	}

	public long getStartupTime() {
		return startupTime;
	}

	public void setStartupTime(long startupTime) {
		this.startupTime = startupTime;
	}

}
