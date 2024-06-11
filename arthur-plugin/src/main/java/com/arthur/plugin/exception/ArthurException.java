package com.arthur.plugin.exception;

import java.io.Serial;

/**
 * 概要描述
 * <p>
 * 详细描述
 *
 * @author DearYang
 * @date 2022-07-18
 * @since 1.0
 */
public class ArthurException extends RuntimeException {


    @Serial
	private static final long serialVersionUID = 8753210365290952109L;

    /**
     * Instantiates a new exception.
     *
     * @param e the e
     */
    public ArthurException(final Throwable e) {
        super(e);
    }

    /**
     * Instantiates a new exception.
     *
     * @param message the message
     */
    public ArthurException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new exception.
     *
     * @param message   the message
     * @param throwable the throwable
     */
    public ArthurException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
