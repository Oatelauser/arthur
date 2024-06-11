package com.arthur.web.reactive.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.Assert;

import java.time.Duration;

/**
 * WebClient配置类
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-01-07
 * @since 1.0
 */
@SuppressWarnings("all")
@ConfigurationProperties(prefix = "arthur.webflux.web-client")
public class WebClientProperties {

	/**
	 * 客户端和服务器建立连接超时，单位秒
	 */
	private int connectTimeout;

	/**
	 * 响应超时时间，单位秒
	 *
	 * @see reactor.netty.http.client.HttpClient#responseTimeout(Duration)
	 */
	private long responseTimeout;

	/**
	 * 指客户端从服务器读取数据包的间隔超时时间,不是总读取时间，单位秒
	 */
	private int readTimeout;

	/**
	 * 客户端写数据到服务器的间隔超时时间，单位秒
	 */
	private int writeTimeout;

	/**
	 * 是否使用全局资源，如果使用全局资源，则默认该配置类所有pool相关的参数设置无效
	 *
	 * @see org.springframework.http.client.reactive.ReactorResourceFactory#setUseGlobalResources(boolean)
	 */
	private boolean poolUseGlobalResources = true;

	/**
	 * 从连接池获取连接的超时时间,不宜过长,单位ms
	 *
	 * @see reactor.netty.resources.ConnectionProvider.Builder#pendingAcquireTimeout(Duration)
	 */
	private Duration poolObtainsConnectionTimout = Duration.ofMillis(200);

	/**
	 * 连接池中连接的最大空闲时间
	 *
	 * @see reactor.netty.resources.ConnectionProvider.Builder#maxIdleTime(Duration)
	 */
	private Duration poolMaxIdleTime;

	/**
	 * 连接池中连接的最大生命周期
	 *
	 * @see reactor.netty.resources.ConnectionProvider.Builder#maxLifeTime(Duration)
	 */
	private Duration poolMaxLifeTime;

	/**
	 * 连接池定期检查过期连接，默认是关闭该功能
	 *
	 * @see reactor.netty.resources.ConnectionProvider.Builder#evictInBackground(Duration)
	 */
	private Duration poolEvictionInterval;

	/**
	 * 连接池的最大连接数
	 *
	 * @see reactor.netty.resources.ConnectionProvider.Builder#maxConnections(int)
	 */
	private int poolMaxConnects = -1;

	/**
	 * 连接池中保留在挂起队列中的最大注册请求数
	 *
	 * @see reactor.netty.resources.ConnectionProvider.Builder#pendingAcquireMaxCount(int)
	 */
	private int poolPendingAcquireMaxCount = -1;

	/**
	 * 连接池的最大活跃线程数
	 *
	 * @see reactor.netty.http.client.Http2AllocationStrategy.Builder#maxConnections(int)
	 */
	private int poolMaxActiveConnects = -1;

	/**
	 * 连接池的最小活跃线程数
	 *
	 * @see reactor.netty.http.client.Http2AllocationStrategy.Builder#minConnections(int)
	 */
	private int poolMinActiveConnects = -1;

	/**
	 * 编解码器的最大内存，Webflux默认是256KB，这里默认是16M
	 */
	private int codecsMaxMemorySize = 16 * 1024 * 1024;

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

	public int getWriteTimeout() {
		return writeTimeout;
	}

	public void setWriteTimeout(int writeTimeout) {
		this.writeTimeout = writeTimeout;
	}

	public int getPoolMaxConnects() {
		return poolMaxConnects;
	}

	public void setPoolMaxConnects(int poolMaxConnects) {
		this.poolMaxConnects = poolMaxConnects;
	}

	public Duration getPoolMaxIdleTime() {
		return poolMaxIdleTime;
	}

	public void setPoolMaxIdleTime(Duration poolMaxIdleTime) {
		this.poolMaxIdleTime = poolMaxIdleTime;
	}

	public Duration getPoolMaxLifeTime() {
		return poolMaxLifeTime;
	}

	public void setPoolMaxLifeTime(Duration poolMaxLifeTime) {
		this.poolMaxLifeTime = poolMaxLifeTime;
	}

	public int getPoolPendingAcquireMaxCount() {
		return poolPendingAcquireMaxCount;
	}

	public void setPoolPendingAcquireMaxCount(int poolPendingAcquireMaxCount) {
		this.poolPendingAcquireMaxCount = poolPendingAcquireMaxCount;
	}

	public Duration getPoolObtainsConnectionTimout() {
		return poolObtainsConnectionTimout;
	}

	public void setPoolObtainsConnectionTimout(Duration poolObtainsConnectionTimout) {
		this.poolObtainsConnectionTimout = poolObtainsConnectionTimout;
	}

	public Duration getPoolEvictionInterval() {
		return poolEvictionInterval;
	}

	public void setPoolEvictionInterval(Duration poolEvictionInterval) {
		this.poolEvictionInterval = poolEvictionInterval;
	}

	public int getPoolMaxActiveConnects() {
		return poolMaxActiveConnects;
	}

	public void setPoolMaxActiveConnects(int poolMaxActiveConnects) {
		this.poolMaxActiveConnects = poolMaxActiveConnects;
	}

	public int getPoolMinActiveConnects() {
		return poolMinActiveConnects;
	}

	public void setPoolMinActiveConnects(int poolMinActiveConnects) {
		this.poolMinActiveConnects = poolMinActiveConnects;
	}

	public int getCodecsMaxMemorySize() {
		return codecsMaxMemorySize;
	}

	public void setCodecsMaxMemorySize(int codecsMaxMemorySize) {
		Assert.state(codecsMaxMemorySize > 0, "codecsMaxMemorySize ["
			+ codecsMaxMemorySize + "] > 0");
		this.codecsMaxMemorySize = codecsMaxMemorySize;
	}

	public boolean getPoolUseGlobalResources() {
		return poolUseGlobalResources;
	}

	public void setPoolUseGlobalResources(boolean poolUseGlobalResources) {
		this.poolUseGlobalResources = poolUseGlobalResources;
	}

}
