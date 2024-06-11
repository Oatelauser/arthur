package com.arthur.gateway.handler;

import com.arthur.sentinel.utils.WebServerResponse;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.WebProperties.Resources;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * 网关全局异常处理器
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2022-11-10
 * @since 1.0
 */
public class GatewayErrorWebExceptionHandler extends DefaultErrorWebExceptionHandler {

	public GatewayErrorWebExceptionHandler(ErrorAttributes errorAttributes, Resources resources,
   			ErrorProperties errorProperties, ApplicationContext applicationContext) {
		super(errorAttributes, resources, errorProperties, applicationContext);
	}

	@Override
	@SuppressWarnings("ThrowableResultOfMethodCallIgnored")
	protected Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
		Throwable throwable = this.getError(request);
		HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		if (throwable instanceof NotFoundException) {
			httpStatus = HttpStatus.SERVICE_UNAVAILABLE;
		} else if (throwable instanceof ResponseStatusException) {
			HttpStatusCode httpStatusCode = ((ResponseStatusException) throwable).getStatusCode();
			httpStatus = Optional.ofNullable(HttpStatus.resolve(httpStatusCode.value())).orElse(httpStatus);
		}

		return ServerResponse.status(httpStatus.value()).contentType(MediaType.APPLICATION_JSON)
			.body(BodyInserters.fromValue(WebServerResponse.ofStatus(httpStatus, throwable.getLocalizedMessage())));
	}

}
