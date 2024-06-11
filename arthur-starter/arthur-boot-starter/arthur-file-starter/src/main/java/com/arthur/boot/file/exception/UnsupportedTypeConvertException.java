package com.arthur.boot.file.exception;


import com.arthur.lang.exception.ArthurException;

import java.io.Serial;

/**
 * 不支持的类型转换异常
 *
 * @author DearYang
 * @date 2022-09-29
 * @since 1.0
 */
@SuppressWarnings("unused")
public class UnsupportedTypeConvertException extends ArthurException {

	@Serial
	private static final long serialVersionUID = 1737007412104335159L;

	public UnsupportedTypeConvertException(String message) {
		super(message);
	}

	public UnsupportedTypeConvertException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnsupportedTypeConvertException(Throwable cause) {
		super(cause);
	}

	public UnsupportedTypeConvertException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
