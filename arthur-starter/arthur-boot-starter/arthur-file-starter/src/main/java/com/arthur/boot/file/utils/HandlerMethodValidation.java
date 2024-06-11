package com.arthur.boot.file.utils;

import org.springframework.core.Conventions;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.util.List;

import static org.springframework.validation.BindingResult.MODEL_KEY_PREFIX;

/**
 * Spring Validation对于控制器方法的校验
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-04-23
 * @see com.arthur.boot.file.handler.ExcelResponseMethodHandler
 * @see com.arthur.boot.file.reslover.HandlerMethodExcelParameterResolver
 * @since 1.0
 */
public class HandlerMethodValidation {

    private static final ExcelMethodProcessor delegate = new ExcelMethodProcessor();

    /**
     * 校验控制器方法上的参数
     *
     * @param data          方法参数对应的值
     * @param parameter     方法参数
     * @param mavContainer  {@link ModelAndViewContainer}
     * @param webRequest    {@link NativeWebRequest}
     * @param binderFactory {@link WebDataBinderFactory}
     * @see RequestResponseBodyMethodProcessor#resolveArgument(MethodParameter, ModelAndViewContainer, NativeWebRequest, WebDataBinderFactory)
     */
    public static void validateParameterIfApplicable(Object data, MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String name = Conventions.getVariableNameForParameter(parameter);
        if (binderFactory == null) {
            return;
        }

        WebDataBinder binder = binderFactory.createBinder(webRequest, data, name);
        delegate.validateIfApplicable(binder, parameter);
        if (binder.getBindingResult().hasErrors() && delegate.isBindExceptionRequired(binder, parameter)) {
            throw new MethodArgumentNotValidException(parameter, binder.getBindingResult());
        }
        if (mavContainer != null) {
            mavContainer.addAttribute(MODEL_KEY_PREFIX + name, binder.getBindingResult());
        }
    }


    @SuppressWarnings("all")
    private static class ExcelMethodProcessor extends RequestResponseBodyMethodProcessor {

        public ExcelMethodProcessor() {
            // 防止报错
            super(List.of(new StringHttpMessageConverter()));
        }

        @Override
        protected void validateIfApplicable(WebDataBinder binder, MethodParameter parameter) {
            super.validateIfApplicable(binder, parameter);
        }

        @Override
        protected boolean isBindExceptionRequired(WebDataBinder binder, MethodParameter parameter) {
            return super.isBindExceptionRequired(binder, parameter);
        }

    }

}
