package com.arthur.web.constant;

import org.springframework.core.Ordered;

/**
 * 远程IP常量类
 *
 * @author DearYang
 * @date 2022-09-12
 * @since 1.0
 */
public interface RemoteIpConstants {

	/**
	 * 未知的IP地址
	 */
	String UNKNOWN_IP_ADDR = "unknown";

	/**
	 * 本地localhost地址
	 */
	String LOCALHOST_IP = "127.0.0.1";

	/**
	 * localhost映射地址
	 */
	String LOCALHOST_MAPPING_IP = "0:0:0:0:0:0:0:1";

	/**
	 * 代理的IP请求头
	 * X-Forwarded-For：Squid 服务代理
	 * Proxy-Client-IP：apache 服务代理
	 * WL-Proxy-Client-IP：weblogic 服务代理
	 * HTTP_CLIENT_IP：有些代理服务器
	 * X-Real-IP：nginx服务代理
	 */
	String[] PROXY_IP_HEADERS = new String[]{ "X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "ALLOW" };

	/**
	 * 远程IP缓存key
	 */
	String REMOTE_IP_ATTR = RemoteIpConstants.class.getName() + "." + "remoteIp";

	/**
	 * 请求IP解析过滤器Order
	 */
	int REMOTE_FILTER_ORDER = Ordered.HIGHEST_PRECEDENCE + 1;

}
