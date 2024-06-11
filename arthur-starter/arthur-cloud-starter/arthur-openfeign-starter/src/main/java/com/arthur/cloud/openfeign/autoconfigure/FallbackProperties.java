package com.arthur.cloud.openfeign.autoconfigure;

import com.arthur.cloud.openfeign.constant.FallbackMode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.Assert;

import static com.arthur.cloud.openfeign.constant.FallbackMode.DEFAULT;

/**
 * 降级配置
 *
 * @author DearYang
 * @date 2022-09-09
 * @since 1.0
 */
@ConfigurationProperties(prefix = "feign.sentinel.fallback")
public class FallbackProperties {

	/**
	 * The fallback mode for sentinel spring-cloud-gateway. choose `redirect` or
	 * `response`.
	 */
	private FallbackMode mode = DEFAULT;

	/**
	 * Response Body for `response` mode.
	 */
	private String responseBody;

	/**
	 * Response Status for `response` mode.
	 */
	private String responseStatus;

	public FallbackMode getMode() {
		return mode;
	}

	public FallbackProperties setMode(FallbackMode mode) {
		this.mode = mode;
		return this;
	}
	public String getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(String responseBody) {
		Assert.hasText(responseBody, "responseBody must has text");
		this.responseBody = responseBody;
	}

	public String getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(String responseStatus) {
		Assert.hasText(responseBody, "responseStatus must has text");
		this.responseStatus = responseStatus;
	}

}
