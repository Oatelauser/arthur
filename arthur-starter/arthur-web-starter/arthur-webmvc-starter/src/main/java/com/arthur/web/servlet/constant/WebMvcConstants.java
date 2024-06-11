package com.arthur.web.servlet.constant;

/**
 * WebMvc常量类
 *
 * @author DearYang
 * @date 2022-09-07
 * @since 1.0
 */
public interface WebMvcConstants {

	/**
	 * 浏览器安全头信息
	 */
	String CONTENT_SECURITY_POLICY = "script-src 'self'";

	/**
	 * 浏览器安全头信息
	 */
	String CONTENT_SECURITY_POLICY_HEADER = "Content-Security-Policy";

	/**
	 * 自定义{@link org.springframework.web.client.RestTemplate}的Bean名称
	 */
	String REST_TEMPLATE_BEAN = "httpRestTemplate";

	/**
	 * 未知的请求或响应内容
	 */
	String UNKNOWN_PAYLOAD = "[unknown]";

}
