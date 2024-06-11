package com.arthur.web.autoconfigure;

import com.arthur.web.model.ServerResponse;
import com.arthur.web.process.ExceptionResponseProcessor;
import com.arthur.web.server.ServerStatus;
import com.arthur.web.server.ServiceException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.Method;
import java.util.stream.Collectors;

import static com.arthur.web.constant.ServerStatusEnum.INVALID_PARAMETER;
import static com.arthur.web.constant.ServerStatusEnum.SERVER_INTERNAL_ERROR;

/**
 * 统一异常响应
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-04-07
 * @since 1.0
 */
@RestControllerAdvice
public class ExceptionHandlerExceptionProcessor {

	private static final Logger LOG = LoggerFactory.getLogger(ExceptionHandlerExceptionProcessor.class);

	private final GlobalExceptionProperties properties;
	private final ExceptionResponseProcessor exceptionResponseProcessor;

	public ExceptionHandlerExceptionProcessor(GlobalExceptionProperties properties,
			ObjectProvider<ExceptionResponseProcessor> processor) {
		this.properties = properties;
		this.exceptionResponseProcessor = processor.getIfAvailable();
	}

	// =============== Validation 异常 =======================
	@ExceptionHandler(ConstraintViolationException.class)
	public ServerResponse constraintViolationExceptionHandler(ConstraintViolationException ex) {
		String message = ex.getConstraintViolations().stream()
			.map(ConstraintViolation::getMessage).collect(Collectors.joining());
		return ServerResponse.ofError(INVALID_PARAMETER.getCode(), message);
	}

	@ExceptionHandler(value = { MethodArgumentNotValidException.class, BindException.class })
	public ServerResponse handlerBindException(BindException ex) {
		String message = "";
		for (ObjectError error : ex.getBindingResult().getAllErrors()) {
			FieldError fieldError = (FieldError) error;
			message = fieldError.getDefaultMessage();
			break;
		}

		return ServerResponse.ofError(INVALID_PARAMETER.getCode(), message);
	}

	// ====================== 兜底异常处理 ===================
	@ExceptionHandler(ServiceException.class)
	public ServerResponse handleServiceException(ServiceException ex, HandlerMethod handlerMethod) {
		Class<?> beanType = handlerMethod.getBeanType();
		Method method = handlerMethod.getMethod();
		LOG.error("控制器[" + beanType.getName() + "]调用方法[" + method.getName() + "]异常", ex);
		return ServerResponse.ofError(ex);
	}

	@ExceptionHandler(Exception.class)
	public ServerResponse handleException(Exception ex, HandlerMethod handlerMethod) {
		LOG.error("控制器[" + handlerMethod.getBeanType().getName() + "]调用方法[" +
			handlerMethod.getMethod().getName() + "]异常", ex);

		if (exceptionResponseProcessor != null) {
			ServerResponse serverResponse = exceptionResponseProcessor.handleException(ex, handlerMethod);
			if (serverResponse != null) {
				return serverResponse;
			}
		}
		if (ex instanceof ServerStatus status) {
			return ServerResponse.ofError(status);
		}

		if (StringUtils.hasText(properties.getCode())) {
			return ServerResponse.ofError(properties.getCode(), properties.getMsg());
		}

		String error = properties.getShowError() ? ex.getLocalizedMessage() :
			SERVER_INTERNAL_ERROR.getMsg();
		return ServerResponse.ofError(SERVER_INTERNAL_ERROR.getCode(), error);
	}


}
