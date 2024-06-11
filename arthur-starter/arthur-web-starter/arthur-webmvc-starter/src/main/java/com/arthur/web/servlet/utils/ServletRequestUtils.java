package com.arthur.web.servlet.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;

import static com.arthur.web.constant.RemoteIpConstants.*;
import static com.arthur.web.utils.WebUtils.getMultistageReverseProxyIp;

/**
 * {@link HttpServletRequest}工具类
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-20
 * @since 1.0
 */
public abstract class ServletRequestUtils {

	/**
	 * @see #resolveRemoteIp(String[], HttpServletRequest)
	 */
	public static String resolveRemoteIp(HttpServletRequest request) {
		return resolveRemoteIp(PROXY_IP_HEADERS, request);
	}

	/**
	 * 解析客户端真实IP地址
	 *
	 * @param headers 请求头
	 * @param request 请求对象
	 * @return 真实IP地址
	 */
	public static String resolveRemoteIp(String[] headers, HttpServletRequest request) {
		String ipAddress = null;
		for (String header : headers) {
			ipAddress = request.getHeader(header);
			if (StringUtils.hasText(ipAddress) && !UNKNOWN_IP_ADDR.equalsIgnoreCase(ipAddress)) {
				break;
			}
		}

		//有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
		String ip = getMultistageReverseProxyIp(ipAddress);

		// 还是不能获取到，最后再通过request.getRemoteAddr()获取
		if (!StringUtils.hasText(ip) || UNKNOWN_IP_ADDR.equalsIgnoreCase(ipAddress)) {
			ip = request.getRemoteAddr();
		}

		// 替换localhost
		if (LOCALHOST_MAPPING_IP.equals(ip)) {
			return LOCALHOST_IP;
		}

		return ip == null ? "" : ip;
	}

}
