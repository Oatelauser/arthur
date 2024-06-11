package com.arthur.sentinel.callback;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import com.arthur.sentinel.autoconfigure.SentinelProperties;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;

import static com.arthur.sentinel.callback.WebfluxRequestOriginParser.getMultistageReverseProxyIp;
import static com.arthur.sentinel.constant.SentinelConstants.*;

/**
 * 解析请求中IP
 *
 * @author DearYang
 * @date 2022-09-12
 * @since 1.0
 */
public class WebRequestOriginParser implements RequestOriginParser {

	private final SentinelProperties sentinelProperties;

	public WebRequestOriginParser(SentinelProperties sentinelProperties) {
		this.sentinelProperties = sentinelProperties;
	}

	@Override
	public String parseOrigin(HttpServletRequest request) {
		String ipAddress = null;
		for (String header : sentinelProperties.getOrigins()) {
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
