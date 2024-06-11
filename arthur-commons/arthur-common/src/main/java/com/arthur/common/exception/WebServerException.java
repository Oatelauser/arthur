package com.arthur.common.exception;

import com.arthur.common.response.ServerResponse;

import java.io.Serial;

/**
 * ServerResponse
 *
 * @author DearYang
 * @date 2022-09-09
 * @since 1.0
 */
@SuppressWarnings("unused")
public class WebServerException extends ArthurException {

	@Serial
	private static final long serialVersionUID = 7123335533940851637L;

	private final ServerResponse serverResponse;

	public WebServerException(ServerResponse serverResponse) {
		this.serverResponse = serverResponse;
	}

	public WebServerException(String message, ServerResponse serverResponse) {
		super(message);
		this.serverResponse = serverResponse;
	}

	public WebServerException(String message, Throwable cause, ServerResponse serverResponse) {
		super(message, cause);
		this.serverResponse = serverResponse;
	}

	public WebServerException(Throwable cause, ServerResponse serverResponse) {
		super(cause);
		this.serverResponse = serverResponse;
	}

	public WebServerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ServerResponse serverResponse) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.serverResponse = serverResponse;
	}

	public ServerResponse getServerResponse() {
		return serverResponse;
	}

}
