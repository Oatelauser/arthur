package com.arthur.web.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 默认的统一异常配置类
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-04-07
 * @since 1.0
 */

@ConfigurationProperties(prefix = "arthur.web.default-exception-response")
public class GlobalExceptionProperties {

	private String code;
	private String msg;

	/**
	 * 是否展示报错异常信息
	 */
	private boolean showError;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public boolean getShowError() {
		return showError;
	}

	public void setShowError(boolean showError) {
		this.showError = showError;
	}

}
