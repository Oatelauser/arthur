package com.arthur.web.servlet.process;

import com.arthur.common.invoke.Lookups;
import com.arthur.web.annotation.SpringMapping;
import com.arthur.web.servlet.constant.SearchStrategy;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.StringValueResolver;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.function.Predicate;

import static com.arthur.boot.utils.AnnotationUtils.synthesizeAnnotation;
import static com.arthur.web.servlet.constant.SearchStrategy.ALL;
import static java.lang.invoke.MethodType.methodType;
import static org.springframework.core.annotation.AnnotatedElementUtils.findMergedAnnotation;

/**
 * 拓展{@link RequestMappingHandlerMapping}功能，支持{@link SpringMapping}注解
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-04-24
 * @see SpringMappingHandlerRegistar
 * @since 1.0
 */
class SpringMappingHandlerMapping extends RequestMappingHandlerMapping {

	private StringValueResolver embeddedValueResolver;
	private final MappingOperator mappingOperator;

	public SpringMappingHandlerMapping(RequestMappingHandlerMapping delegate) {
		this.mappingOperator = new MappingOperator(delegate);
	}

	@Override
	public void detectHandlerMethods(@NonNull Object handler) {
		super.detectHandlerMethods(handler);
	}

	@Nullable
	@Override
	public RequestMappingInfo getMappingForMethod(@NonNull Method method, @Nullable Class<?> handlerType) {
		return getMappingForElement(method, ALL, handlerType, ALL);
	}

	/**
	 * 获取{@link RequestMappingInfo}
	 *
	 * @param element {@link AnnotatedElement}
	 * @param strategy element的{@link SearchStrategy}注解扫描策略
	 * @param handlerType 控制器类型
	 * @param handlerStrategy handlerType的{@link SearchStrategy}注解扫描策略
	 * @return {@link RequestMappingInfo}
	 */
	public RequestMappingInfo getMappingForElement(AnnotatedElement element, SearchStrategy strategy,
			@Nullable Class<?> handlerType, SearchStrategy handlerStrategy) {
		RequestMappingInfo info = createRequestMappingInfo(element, strategy);
		if (info != null) {
			RequestMappingInfo typeInfo = createRequestMappingInfo(handlerType, handlerStrategy);
			if (typeInfo != null) {
				info = typeInfo.combine(info);
			}
			String prefix = this.getPathPrefix(handlerType);
			if (prefix != null) {
				RequestMappingInfo.BuilderConfiguration config = getBuilderConfiguration();
				info = RequestMappingInfo.paths(prefix).options(config).build().combine(info);
			}
		}
		return info;
	}

	public RequestMappingInfo createRequestMappingInfo(AnnotatedElement element, SearchStrategy strategy) {
		if (strategy == null) {
			strategy = ALL;
		}

		RequestMapping requestMapping = switch (strategy) {
			case REQUEST_MAPPING -> findMergedAnnotation(element, RequestMapping.class);
			case SPRING_MAPPING -> {
				SpringMapping springMapping = findMergedAnnotation(element, SpringMapping.class);
				yield synthesizeAnnotation(springMapping, RequestMapping.class, element);
			}
			case ALL -> {
				RequestMapping annotation = findMergedAnnotation(element, RequestMapping.class);
				if (annotation == null) {
					SpringMapping springMapping = findMergedAnnotation(element, SpringMapping.class);
					if (springMapping != null) {
						annotation = synthesizeAnnotation(springMapping, RequestMapping.class, element);
					}
				}
				yield annotation;
			}
		};

		if (requestMapping == null) {
			return null;
		}

		if (element instanceof Parameter parameter) {
			element = parameter.getDeclaringExecutable();
		}
		RequestCondition<?> requestCondition = element instanceof Class<?> clazz ?
			mappingOperator.getCustomTypeCondition(clazz) :
			mappingOperator.getCustomMethodCondition((Method) element);
		return this.createRequestMappingInfo(requestMapping, requestCondition);
	}

	public RequestMappingInfo createSpringMappingInfo(AnnotatedElement element) {
		SpringMapping springMapping = findMergedAnnotation(element, SpringMapping.class);
		if (springMapping == null) {
			return null;
		}

		RequestMapping requestMapping = synthesizeAnnotation(springMapping, RequestMapping.class, element);
		RequestCondition<?> requestCondition = element instanceof Class<?> clazz ?
			mappingOperator.getCustomTypeCondition(clazz) :
			mappingOperator.getCustomMethodCondition((Method) element);
		return this.createRequestMappingInfo(requestMapping, requestCondition);
	}

	@NonNull
	@Override
	public RequestMappingInfo createRequestMappingInfo(@NonNull RequestMapping requestMapping,
		RequestCondition<?> customCondition) {
		return super.createRequestMappingInfo(requestMapping, customCondition);
	}

	@Override
	@SuppressWarnings("all")
	public void setEmbeddedValueResolver(StringValueResolver resolver) {
		this.embeddedValueResolver = resolver;
		super.setEmbeddedValueResolver(resolver);
	}

	protected String getPathPrefix(Class<?> handlerType) {
		for (Map.Entry<String, Predicate<Class<?>>> entry : getPathPrefixes().entrySet()) {
			if (entry.getValue().test(handlerType)) {
				String prefix = entry.getKey();
				if (this.embeddedValueResolver != null) {
					prefix = this.embeddedValueResolver.resolveStringValue(prefix);
				}
				return prefix;
			}
		}
		return null;
	}

	@Override
	@SuppressWarnings("all")
	public void registerMapping(RequestMappingInfo mapping, Object handler, Method method) {
		mappingOperator.delegate.registerMapping(mapping, handler, method);
	}

	@Override
	@SuppressWarnings("all")
	protected void registerHandlerMethod(Object handler, Method method, RequestMappingInfo mapping) {
		mappingOperator.registerHandlerMethod(handler, method, mapping);
	}

	private static class MappingOperator {

		private static final MethodHandle registerHandlerMethod;
		private static final MethodHandle typeConditionMethod;
		private static final MethodHandle methodConditionMethod;

		static {
			MethodHandles.Lookup lookup = Lookups.trustedLookup(RequestMappingHandlerMapping.class);
			try {
				registerHandlerMethod = lookup.findVirtual(RequestMappingHandlerMapping.class,
					"registerHandlerMethod", methodType(void.class, Object.class, Method.class, RequestMappingInfo.class));
				typeConditionMethod = lookup.findVirtual(RequestMappingHandlerMapping.class,
					"getCustomTypeCondition", methodType(RequestCondition.class, Class.class));
				methodConditionMethod = lookup.findVirtual(RequestMappingHandlerMapping.class,
					"getCustomMethodCondition", methodType(RequestCondition.class, Method.class));
			} catch (Exception ex) {
				throw new IllegalStateException(ex);
			}
		}

		private final RequestMappingHandlerMapping delegate;

		private MappingOperator(RequestMappingHandlerMapping requestMappingHandlerMapping) {
			this.delegate = requestMappingHandlerMapping;
		}

		private void registerHandlerMethod(Object handler, Method method, RequestMappingInfo mapping) {
			try {
				registerHandlerMethod.invoke(delegate, handler, method, mapping);
			} catch (Throwable ex) {
				throw new IllegalStateException("Cannot register HandlerMethod[" + mapping + "]", ex);
			}
		}

		private RequestCondition<?> getCustomTypeCondition(Class<?> handlerType) {
			try {
				return (RequestCondition<?>) typeConditionMethod.invoke(delegate, handlerType);
			} catch (Throwable ex) {
				throw new IllegalStateException(ex);
			}
		}

		private RequestCondition<?> getCustomMethodCondition(Method element) {
			try {
				return (RequestCondition<?>) methodConditionMethod.invoke(delegate, element);
			} catch (Throwable ex) {
				throw new IllegalStateException(ex);
			}
		}
	}

}
