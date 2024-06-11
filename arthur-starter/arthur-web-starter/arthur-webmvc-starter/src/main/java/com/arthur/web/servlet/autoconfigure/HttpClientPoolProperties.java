package com.arthur.web.servlet.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * HTTP请求连接池配置
 *
 * @author DearYang
 * @date 2022-09-16
 * @since 1.0
 */
@ConfigurationProperties(prefix = "spring.http-client.pool")
public class HttpClientPoolProperties {

	/**
	 * 连接池的最大连接数
	 */
	private int maxTotalConnect;

	/**
	 * 同路由的并发数
	 */
	private int maxConnectPerRoute;

	/**
	 * 客户端和服务器建立连接超时，默认2s
	 */
	private int connectTimeout = 2 * 1000;

	/**
	 * 指客户端从服务器读取数据包的间隔超时时间,不是总读取时间，默认30s
	 */
	private int readTimeout = 30 * 1000;

	private String charset = StandardCharsets.UTF_8.name();

	/**
	 * 重试次数,默认2次
	 */
	private int retryTimes = 2;

	/**
	 * 从连接池获取连接的超时时间,不宜过长,单位ms
	 */
	private int connectionRequestTimout = 200;

	/**
	 * 针对不同的地址,特别设置不同的长连接保持时间
	 */
	private Map<String, Integer> keepAliveTargetHost;

	/**
	 * 针对不同的地址,特别设置不同的长连接保持时间,单位s
	 */
	private int keepAliveTime = 60;

	public int getMaxTotalConnect() {
		return maxTotalConnect;
	}

	public void setMaxTotalConnect(int maxTotalConnect) {
		this.maxTotalConnect = maxTotalConnect;
	}

	public int getMaxConnectPerRoute() {
		return maxConnectPerRoute;
	}

	public void setMaxConnectPerRoute(int maxConnectPerRoute) {
		this.maxConnectPerRoute = maxConnectPerRoute;
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public int getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public int getRetryTimes() {
		return retryTimes;
	}

	public void setRetryTimes(int retryTimes) {
		this.retryTimes = retryTimes;
	}

	public int getConnectionRequestTimout() {
		return connectionRequestTimout;
	}

	public void setConnectionRequestTimout(int connectionRequestTimout) {
		this.connectionRequestTimout = connectionRequestTimout;
	}

	public Map<String, Integer> getKeepAliveTargetHost() {
		return keepAliveTargetHost;
	}

	public void setKeepAliveTargetHost(Map<String, Integer> keepAliveTargetHost) {
		this.keepAliveTargetHost = keepAliveTargetHost;
	}

	public int getKeepAliveTime() {
		return keepAliveTime;
	}

	public void setKeepAliveTime(int keepAliveTime) {
		this.keepAliveTime = keepAliveTime;
	}

}
