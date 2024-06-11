package com.arthur.plugin.ssl.constant;

import com.arthur.web.server.ServerStatus;

/**
 * SSL响应枚举类
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-03-29
 * @since 1.0
 */
public enum ServerStatusEnum implements ServerStatus {

	REVOKED_CERT("SSL100", "无效的证书:吊销证书检查该证书已经过期"),
	;

	private final String code;
	private final String msg;

	ServerStatusEnum(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getMsg() {
		return msg;
	}

}
