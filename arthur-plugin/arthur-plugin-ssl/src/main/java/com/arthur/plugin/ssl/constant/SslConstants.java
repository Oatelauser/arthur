package com.arthur.plugin.ssl.constant;

/**
 * SSL常量类
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-03-21
 * @since 1.0
 */
public interface SslConstants {

	/**
	 * HTTPS协议标识
	 */
	String HTTPS_PROTOCOL_NAME = "https";

	/**
	 * 标识是HTTPS请求
	 */
	String HTTPS_PROTOCOL_ATTR = SslConstants.class.getName() + "." + "https";

	/**
	 * 设置基于路由的SSL的{@link reactor.netty.http.client.HttpClient}
	 */
	String ROUTE_HTTPCLIENT_ATTR = SslConstants.class.getName() + "." + "routeHttpClient";

}
