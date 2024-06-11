package com.arthur.boot.file.handler;

import com.arthur.boot.file.annotation.ExcelMapping;
import com.arthur.boot.file.annotation.ExcelParam;
import com.arthur.boot.file.annotation.ExcelResponse;
import com.arthur.boot.file.excel.ExcelDataEntity;
import com.arthur.boot.file.reslover.HandlerMethodExcelParameterResolver;
import com.arthur.web.servlet.process.HandlerMethodProcessor;
import com.arthur.web.servlet.process.SpringMappingHandlerRegistar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.lang.NonNull;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Set;

import static com.arthur.web.servlet.constant.SearchStrategy.REQUEST_MAPPING;
import static com.arthur.web.servlet.constant.SearchStrategy.SPRING_MAPPING;

/**
 * 处理{@link ExcelMapping}，动态生成接口映射{@link RequestMapping}
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-04-20
 * @since 1.0
 */
@SuppressWarnings("SpellCheckingInspection")
public class ExcelMappingHandlerMapping implements HandlerMethodProcessor, BeanClassLoaderAware {

	private static final Logger LOG = LoggerFactory.getLogger(ExcelMappingHandlerMapping.class);

	private final ResolvableType PARAMETER_TYPE = ResolvableType.forClass(Collection.class);

    private ClassLoader beanClassLoader;
    private final SpringMappingHandlerRegistar handlerMappingRegistar;

    public ExcelMappingHandlerMapping(SpringMappingHandlerRegistar handlerMappingRegistar) {
        this.handlerMappingRegistar = handlerMappingRegistar;
    }

    @Override
    public boolean supports(Set<String> urls, HandlerMethod handlerMethod) {
        return findMethodParameterIfPossible(handlerMethod) != null;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void handleMethod(Set<String> urls, RequestMappingInfo requestMappingInfo, HandlerMethod handlerMethod) {
		MethodParameter methodParameter = this.findMethodParameterIfPossible(handlerMethod);
		RequestMappingInfo info = handlerMappingRegistar.getMappingForElement(
			methodParameter.getParameter(), SPRING_MAPPING, handlerMethod.getBeanType(), REQUEST_MAPPING);

		Class<?> parameterType = this.resolveParameterGenericType(methodParameter);
		if (parameterType != null && parameterType != Object.class) {
			handlerMappingRegistar.registerRequestMapping(OperationHandler.handlerMethod,
				this.adaptMethod(parameterType), info);
			return;
		}

		if (LOG.isInfoEnabled()) {
			LOG.info("Cannot register @ExcelMapping, because the argument type could not be resolved");
		}
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private Object adaptMethod(Class<?> parameterType) {
        Enhancer enhancer = new Enhancer();
        enhancer.setClassLoader(this.beanClassLoader);
        enhancer.setSuperclass(OperationHandler.class);
        enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) ->
                switch (method.getName()) {
                    case "hashCode" -> 0;
                    case "equals" -> true;
                    case "downloadTemplate" -> new ExcelDataEntity(parameterType, null);
                    default -> null;
                });
        return enhancer.create();
    }

    private MethodParameter findMethodParameterIfPossible(HandlerMethod handlerMethod) {
        for (MethodParameter methodParameter : handlerMethod.getMethodParameters()) {
            if (methodParameter.hasParameterAnnotation(ExcelParam.class)
					&& methodParameter.hasParameterAnnotation(ExcelMapping.class)) {
                return methodParameter;
            }
        }
        return null;
    }

	/**
	 * 解析方法参数上的类型
	 *
	 * @param methodParameter 方法参数
	 * @return 方法参数类型
	 */
	private Class<?> resolveParameterGenericType(MethodParameter methodParameter) {
		ResolvableType resolvableType = ResolvableType.forMethodParameter(methodParameter);
		// 如果是Collection的实现类，则直接提取泛型对象
		if (PARAMETER_TYPE.isAssignableFrom(resolvableType)) {
			return HandlerMethodExcelParameterResolver.resolveParameterGenericType(methodParameter);
		}

		// 否则默认解析泛型参数
		Type parameterType = methodParameter.getGenericParameterType();
		if (parameterType instanceof Class<?> parameterClass) {
			return parameterClass;
		}

		Class<?> resolveParameterClass = resolvableType.resolve();
		if (resolveParameterClass != null && Collection.class.isAssignableFrom(resolveParameterClass)) {
			return HandlerMethodExcelParameterResolver.resolveParameterGenericType(methodParameter);
		}
		return null;
	}

    @Override
    public void setBeanClassLoader(@NonNull ClassLoader classLoader) {
        this.beanClassLoader = classLoader;
    }

    static class OperationHandler {

        private static final Method handlerMethod;

        static {
            handlerMethod = ReflectionUtils.findMethod(OperationHandler.class, "downloadTemplate");
        }

        @SuppressWarnings("unused")
        @ExcelResponse(fileName = "上传模板")
        public ExcelDataEntity<?> downloadTemplate() {
            return null;
        }

    }

}
