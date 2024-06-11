package com.arthur.web.server;

import com.arthur.common.exception.ArthurException;
import com.arthur.web.autoconfigure.ExceptionHandlerExceptionProcessor;

import java.io.Serial;

/**
 * 服务统一返回异常，该类可以被{@link ExceptionHandlerExceptionProcessor}处理
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-04-07
 * @since 1.0
 */
public class ServiceException extends ArthurException implements ServerStatus {

	@Serial
	private static final long serialVersionUID = 7885444567426226028L;

	private final String code;
	private final String msg;

	public ServiceException(ServerStatus status) {
		this.code = status.getCode();
		this.msg = status.getMsg();
	}

	public ServiceException(ServerStatus status, Throwable cause) {
		super(status.getMsg(), cause);
		this.code = status.getCode();
		this.msg = status.getMsg();
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
