package com.arthur.oauth2.exception;

import org.springframework.core.NestedRuntimeException;

import java.io.Serial;

/**
 * 响应状态异常
 *
 * @author DearYang
 * @date 2022-08-18
 * @since 1.0
 */
public class HttpStatusException extends NestedRuntimeException {

    @Serial
	private static final long serialVersionUID = -4183967813350402866L;
    private static final String ERROR_MESSAGE = "响应状态异常[%s]";

    private final String code;

    public HttpStatusException(String code) {
        this(code, String.format(ERROR_MESSAGE, code));
    }

    public HttpStatusException(String code, String msg) {
        super(msg);
        this.code = code;
    }

    public HttpStatusException(String code, String msg, Throwable cause) {
        super(msg, cause);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
