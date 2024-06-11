package com.arthur.web.process;

import com.arthur.web.annotation.ExceptionResponse;
import com.arthur.web.annotation.ExceptionResponses;
import com.arthur.web.model.ServerResponse;
import org.springframework.lang.NonNull;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.Method;
import java.util.*;

import static com.arthur.boot.utils.AnnotationUtils.findClassMergedRepeatableAnnotations;
import static com.arthur.boot.utils.AnnotationUtils.findMethodMergedRepeatableAnnotations;

/**
 * 处理{@link ExceptionResponse}注解
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-04-18
 * @since 1.0
 */
public class ExceptionResponseProcessor {

	protected final Map<HandlerMethod, Set<ExceptionResponseEntity>> handlerEntities = new HashMap<>();

	/**
	 * 处理控制器方法（接口方法），加载{@link ExceptionResponse}
	 *
	 * @param handlerMethod 控制器方法
	 */
	public void processHandlerMethod(HandlerMethod handlerMethod) {
		if (handlerEntities.containsKey(handlerMethod)) {
			return;
		}

		// 1.解析方法上的注解
		List<ExceptionResponseEntity> methodEntities = this.resolveMethodExceptionResponse(handlerMethod.getMethod());
		Set<ExceptionResponseEntity> chain = new TreeSet<>(methodEntities);

		// 2.解析方法异常类中的注解
		List<ExceptionResponseEntity> exceptionEntities = this.resolveExceptionClassExceptionResponse(handlerMethod.getMethod());
		chain.addAll(exceptionEntities);

		// 3.解析方法所在类上的注解
		List<ExceptionResponseEntity> classEntities = this.resolveClassExceptionResponse(handlerMethod.getBeanType());
		chain.addAll(classEntities);

		if (!CollectionUtils.isEmpty(classEntities)) {
			handlerEntities.put(handlerMethod, chain);
		}
	}

	/**
	 * 基于缓存的兜底异常处理，返回异常响应
	 *
	 * @param exception     异常
	 * @param handlerMethod 控制器方法
	 * @return {@link ServerResponse}
	 */
	public ServerResponse handleException(Exception exception, HandlerMethod handlerMethod) {
		Set<ExceptionResponseEntity> entities = handlerEntities.get(handlerMethod);
		if (entities == null) {
			return null;
		}

		for (ExceptionResponseEntity entity : entities) {
			if (entity.exception.isAssignableFrom(exception.getClass())) {
				String msg = entity.showException ? exception.getLocalizedMessage() : entity.msg;
				return ServerResponse.ofError(entity.code, msg);
			}
		}

		return null;
	}

	/**
	 * 解析方法上的{@link ExceptionResponse}注解
	 *
	 * @param method 方法对象
	 * @return {@link ExceptionResponseEntity}
	 */
	protected List<ExceptionResponseEntity> resolveMethodExceptionResponse(Method method) {
		Set<ExceptionResponse> annotations = findMethodMergedRepeatableAnnotations(
			method, ExceptionResponse.class, ExceptionResponses.class);
		if (CollectionUtils.isEmpty(annotations)) {
			return List.of();
		}
		return this.buildExceptionResponseEntity(2, annotations);
	}

	/**
	 * 解析方法上的定义的异常列表
	 *
	 * @param method 控制器方法
	 * @return {@link ExceptionResponseEntity}
	 */
	protected List<ExceptionResponseEntity> resolveExceptionClassExceptionResponse(Method method) {
		Class<?>[] exceptionTypes = method.getExceptionTypes();
		if (ObjectUtils.isEmpty(exceptionTypes)) {
			return List.of();
		}

		List<ExceptionResponseEntity> entities = new ArrayList<>();
		for (Class<?> exceptionType : exceptionTypes) {
			Set<ExceptionResponse> classAnnotations = findClassMergedRepeatableAnnotations(
				exceptionType, ExceptionResponse.class, ExceptionResponses.class);
			if (!CollectionUtils.isEmpty(classAnnotations)) {
				List<ExceptionResponseEntity> classEntities = this.buildExceptionResponseEntity(1, classAnnotations);
				entities.addAll(classEntities);
			}
		}
		return entities;
	}

	/**
	 * 解析类上的{@link ExceptionResponse}注解
	 *
	 * @param klass 类对象
	 * @return {@link ExceptionResponseEntity}
	 */
	protected List<ExceptionResponseEntity> resolveClassExceptionResponse(Class<?> klass) {
		Set<ExceptionResponse> annotations = findClassMergedRepeatableAnnotations(klass,
			ExceptionResponse.class, ExceptionResponses.class);
		if (CollectionUtils.isEmpty(annotations)) {
			return List.of();
		}
		return this.buildExceptionResponseEntity(0, annotations);
	}

	private List<ExceptionResponseEntity> buildExceptionResponseEntity(int level,
		Set<ExceptionResponse> methodRepeatableAnnotations) {
		List<ExceptionResponseEntity> entities = new ArrayList<>(methodRepeatableAnnotations.size());
		for (ExceptionResponse annotation : methodRepeatableAnnotations) {
			for (Class<? extends Throwable> exception : annotation.value()) {
				ExceptionResponseEntity entity = new ExceptionResponseEntity();
				entity.level = level;
				entity.exception = exception;
				entity.code = annotation.code();
				entity.msg = annotation.msg();
				entity.showException = annotation.showException();
				entities.add(entity);
			}
		}
		return entities;
	}

	static class ExceptionResponseEntity implements Comparable<ExceptionResponseEntity> {

		private int level;
		private String code;
		private String msg;
		private boolean showException;
		private Class<? extends Throwable> exception;

		@Override
		public int compareTo(@NonNull ExceptionResponseEntity entity) {
			if (this.equals(entity)) {
				return 0;
			}
			if (this.level > entity.level) {
				return -1;
			}
			if (this.level < entity.level) {
				return 1;
			}
			if (this.exception.equals(entity.exception)) {
				return 0;
			}

			return this.exception.isAssignableFrom(entity.exception) ? 1 : -1;
		}

	}

}
