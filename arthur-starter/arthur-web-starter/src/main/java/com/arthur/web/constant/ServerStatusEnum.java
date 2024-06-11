package com.arthur.web.constant;

import com.arthur.web.server.ServerStatus;

/**
 * 通用响应枚举
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-04-04
 * @since 1.0
 */
public enum ServerStatusEnum implements ServerStatus {

	SERVER_INTERNAL_ERROR("WEB999", "服务内部异常"),
	INVALID_PARAMETER("WEB110", "无效的参数异常"),
	EMPTY_DATA("WEB100", "查询的数据为空"),
	SAVE_DATA_FAIL("WEB101", "保存数据失败"),
	;

	final String code;
	final String msg;

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
