package com.arthur.plugin.route.core;

import org.springframework.cloud.gateway.route.RouteDefinition;

import java.util.Map;

/**
 * 拓展网关路由的信息定义
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2022-12-30
 * @since 1.0
 */
@SuppressWarnings("unused")
public class GatewayRouteDefinition extends RouteDefinition {

	/**
	 * 负载均衡算法名称
	 */
	private String lb;

	/**
	 * 路由权重
	 */
	private Integer weight;

	/**
	 * 服务预热
	 */
	private Integer warmup;

	/**
	 * Nacos环境
	 */
	private String env;

	/**
	 * Nacos命名空间
	 */
	private String namespace;

	/**
	 * 参数
	 */
	private Map<String, String> args;

	public GatewayRouteDefinition() {
	}

	public GatewayRouteDefinition(String text) {
		super(text);
	}

	public String getLb() {
		return lb;
	}

	public void setLb(String lb) {
		this.lb = lb;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Integer getWarmup() {
		return warmup;
	}

	public void setWarmup(Integer warmup) {
		this.warmup = warmup;
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

	public Map<String, String> getArgs() {
		return args;
	}

	public void setArgs(Map<String, String> args) {
		this.args = args;
	}

}
