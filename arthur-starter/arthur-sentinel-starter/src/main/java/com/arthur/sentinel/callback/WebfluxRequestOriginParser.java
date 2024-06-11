package com.arthur.sentinel.callback;

import com.arthur.common.constant.BaseConstants;
import com.arthur.sentinel.autoconfigure.SentinelProperties;
import com.arthur.sentinel.constant.SentinelConstants;
import com.arthur.sentinel.constant.WebfluxConstants;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;

import java.net.InetSocketAddress;

import static com.arthur.common.utils.StringUtils.split;
import static com.arthur.sentinel.constant.SentinelConstants.LOCALHOST_MAPPING_IP;

/**
 * 解析请求中的IP
 *
 * @author DearYang
 * @date 2022-09-12
 * @since 1.0
 */
public class WebfluxRequestOriginParser implements WebfluxOriginParser {

	private final SentinelProperties sentinelProperties;

	public WebfluxRequestOriginParser(SentinelProperties sentinelProperties) {
		this.sentinelProperties = sentinelProperties;
	}

	@Override
	public String parseOrigin(ServerWebExchange exchange) {
		String ipAddress = null;
		HttpHeaders headers = exchange.getRequest().getHeaders();
		for (String header : sentinelProperties.getOrigins()) {
			ipAddress = headers.getFirst(header);
			if (StringUtils.hasText(ipAddress) && !SentinelConstants.UNKNOWN_IP_ADDR.equalsIgnoreCase(ipAddress)) {
				break;
			}
		}

		//有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
		String ip = getMultistageReverseProxyIp(ipAddress);
		// 还是不能获取到，最后再通过request.getRemoteAddr()获取
		if (!StringUtils.hasText(ip) || SentinelConstants.UNKNOWN_IP_ADDR.equalsIgnoreCase(ipAddress)) {
			InetSocketAddress remoteAddress = exchange.getRequest().getRemoteAddress();
			if (remoteAddress != null) {
				ip = remoteAddress.getAddress().getHostAddress();
			}
		}
		// 替换localhost
		if (LOCALHOST_MAPPING_IP.equals(ip)) {
			ip = SentinelConstants.LOCALHOST_IP;
		} else if (ip == null){
			ip = "";
		}

		exchange.getAttributes().put(WebfluxConstants.REMOTE_IP_ATTR, ip);
		return ip;
	}

	static String getMultistageReverseProxyIp(String ip) {
		if (StringUtils.hasText(ip)) {
			for (String ipAddresses : split(ip.trim(), BaseConstants.COMMA)) {
				if (!SentinelConstants.UNKNOWN_IP_ADDR.equalsIgnoreCase(ipAddresses)) {
					ip = ipAddresses;
					break;
				}
			}
		}
		return ip;
	}

}
