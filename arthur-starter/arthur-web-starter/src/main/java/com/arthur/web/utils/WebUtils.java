package com.arthur.web.utils;

import com.arthur.common.constant.BaseConstants;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static com.arthur.common.utils.StringUtils.split;
import static com.arthur.web.constant.RemoteIpConstants.UNKNOWN_IP_ADDR;

/**
 * Web工具类
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-13
 * @since 1.0
 */
public abstract class WebUtils {

	/**
	 * 通过HTTP头信息获取字符编码
	 *
	 * @param headers HTTP头信息
	 * @return 字符编码
	 */
	public static Charset resolveHeaderCharacter(HttpHeaders headers) {
		MediaType contentType = headers.getContentType();
		if (contentType != null) {
			Charset charset = contentType.getCharset();
			if (charset != null) {
				return charset;
			}
		}
		return StandardCharsets.UTF_8;
	}

	/**
	 * 有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
	 *
	 * @param ip 请求IP
	 * @return 返回客户端真实IP
	 */
	public static String getMultistageReverseProxyIp(String ip) {
		if (!StringUtils.hasText(ip)) {
			return ip;
		}

		for (String ipAddresses : split(ip.trim(), BaseConstants.COMMA)) {
			if (!UNKNOWN_IP_ADDR.equalsIgnoreCase(ipAddresses)) {
				ip = ipAddresses;
				break;
			}
		}
		return ip;
	}

}
