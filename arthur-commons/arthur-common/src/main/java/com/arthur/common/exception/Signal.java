package com.arthur.common.exception;

import java.io.Serial;

/**
 * 信号通知
 *
 * @author DearYang
 * @date 2022-08-19
 * @since 1.0
 */
@SuppressWarnings("unused")
public class Signal extends Error {

    @Serial
	private static final long serialVersionUID = 2892282037853353647L;

    private String name;

    public Signal() {
    }

    public Signal(String name) {
        this.name = name;
    }

    @Override
    public synchronized Throwable initCause(Throwable cause) {
        return this;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

	public String getName() {
		return name;
	}

}
