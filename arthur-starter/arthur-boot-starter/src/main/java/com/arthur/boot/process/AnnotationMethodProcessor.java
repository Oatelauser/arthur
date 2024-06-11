package com.arthur.boot.process;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 标识注解方法的处理器
 *
 * @author DearYang
 * @date 2022-08-28
 * @see AnnotationListenerMethodPostProcessor
 * @since 1.0
 */
public interface AnnotationMethodProcessor<T extends Annotation> {

    /**
     * 获取注解类型
     *
     * @return 注解类型
     */
    Class<T> getAnnotationType();

    /**
     * 是否支持
     *
     * @param annotatedMethod 标识注解的方法
     * @return yes or no
     */
    boolean supports(Method annotatedMethod);

    /**
     * 处理标识注解的方法
     *
     * @param beanName        bean name
     * @param targetType      bean所在的class
     * @param annotatedMethod 标识注解的方法
     */
    void processMethod(String beanName, Class<?> targetType, Method annotatedMethod);

}
