package com.arthur.lang.exception;

import java.io.Serial;

import static com.arthur.lang.ArthurLangConstants.MAX_SEARCH_DEEP;


/**
 * 父类异常
 *
 * @author DearYang
 * @date 2022-08-19
 * @since 1.0
 */
@SuppressWarnings("unused")
public class ArthurException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = -1626353896401745771L;

	public ArthurException() {
	}

	public ArthurException(String message) {
		super(message);
	}

	public ArthurException(String message, Throwable cause) {
		super(message, cause);
	}

	public ArthurException(Throwable cause) {
		super(cause);
	}

	public ArthurException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * 检索给定异常的最深层原因（如果有）
	 * <p>
	 * copy by {@code org.springframework.core.NestedRuntimeException#getRootCause()}
	 *
	 * @return 最里面的异常，如果没有则为null
	 */
	public Throwable getRootCause() {
		Throwable prev = null;
		Throwable current = this;
		while (current != null && current != prev) {
			prev = current;
			current = current.getCause();
		}

		return prev;
	}

	/**
	 * 检索此异常的最具体原因，即最内部原因（根本原因）或此异常本身。
	 * 与getRootCause()的不同之处在于，如果没有根本原因，它会回退到当前异常
	 * <p>
	 * copy by {@code org.springframework.core.NestedRuntimeException#getMostSpecificCause()}
	 *
	 * @return 最具体的原因（从不为null ）
	 */
	public Throwable getMostSpecificCause() {
		Throwable rootCause = getRootCause();
		return rootCause == null ? this : rootCause;
	}

	/**
	 * 检查此异常是否包含给定类型的异常：要么属于给定类本身，要么包含给定类型的嵌套原因
	 * <p>
	 * copy by {@code org.springframework.core.NestedRuntimeException#contains(java.lang.Class)}
	 *
	 * @param exType 要查找的异常类型
	 * @return 是否存在指定类型的异常
	 */
	public boolean contains(Class<?> exType) {
		if (exType == null) {
			return false;
		}
		if (exType.isInstance(this)) {
			return true;
		}
		Throwable cause = getCause();
		if (cause == this) {
			return false;
		}
		if (cause instanceof ArthurException) {
			return ((ArthurException) cause).contains(exType);
		} else {
			while (cause != null) {
				if (exType.isInstance(cause)) {
					return true;
				}
				if (cause.getCause() == cause) {
					break;
				}
				cause = cause.getCause();
			}
			return false;
		}
	}

	/**
	 * 判断是否{@link ArthurException}异常
	 *
	 * @param t 异常类
	 * @return yes or no
	 */
	public static boolean isArthurException(Throwable t) {
		if (t == null) {
			return false;
		}

		int counter = 0;
		for (Throwable cause = t; counter < MAX_SEARCH_DEEP; counter++, cause = cause.getCause()) {
			if (cause instanceof ArthurException) {
				return true;
			}
		}

		return false;
	}

}
