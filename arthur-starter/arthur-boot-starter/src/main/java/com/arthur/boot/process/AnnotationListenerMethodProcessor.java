package com.arthur.boot.process;

import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.NonNull;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

import static java.lang.reflect.Modifier.*;

/**
 * resolve annotated bean method
 *
 * @author DearYang
 * @date 2022-07-28
 * @see AnnotationListenerMethodPostProcessor
 * @since 1.0
 */
@Deprecated
public abstract class AnnotationListenerMethodProcessor<T extends Annotation> implements ApplicationListener<ContextRefreshedEvent> {

    private final Class<T> annotationType;

    public AnnotationListenerMethodProcessor() {
        annotationType = resolveGenericType(getClass());
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> resolveGenericType(Class<?> declaredClass) {
        ParameterizedType parameterizedType = (ParameterizedType) declaredClass.getGenericSuperclass();
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        return (Class<T>) actualTypeArguments[0];
    }

    @Override
    public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        processBeans(applicationContext);
    }

    private void processBeans(ApplicationContext applicationContext) {
        Map<String, Object> beans = applicationContext.getBeansOfType(Object.class, false, false);
        processBeans(beans, applicationContext);
    }

    private void processBeans(Map<String, Object> beans, ApplicationContext applicationContext) {
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            String beanName = entry.getKey();
            Object bean = entry.getValue();
            if (bean != null) {
                Class<?> beanClass = AopUtils.getTargetClass(bean);
                processBean(beanName, bean, beanClass, applicationContext);
            }
        }
    }

    private void processBean(String beanName, Object bean, Class<?> beanClass, ApplicationContext applicationContext) {
        ReflectionUtils.doWithMethods(beanClass, method -> {
            T annotation = AnnotationUtils.getAnnotation(method, annotationType);
            if (annotation != null && isCandidateMethod(bean, beanClass, annotation, method, applicationContext)) {
                processListenerMethod(beanName, bean, beanClass, annotation, method, applicationContext);
            }
        }, this::isListenerMethod);
    }

    /**
     * 判断监听方法
     * <p>
     * 1.必须是public访问修饰符
     * 2.不能是静态方法
     * 3.不能是native方法
     * 4.不能是抽象方法
     *
     * @param method 监听方法
     * @return 是否过滤
     */
    protected boolean isListenerMethod(Method method) {
        int modifiers = method.getModifiers();
        return isPublic(modifiers) && !isStatic(modifiers) && !isNative(modifiers) && !isAbstract(modifiers);
    }

    public boolean isCandidateMethod(Object bean, Class<?> beanClass, T annotation, Method method, ApplicationContext applicationContext) {
        return true;
    }

    /**
     * 处理监听的方法
     *
     * @param beanName           bean name
     * @param bean               bean
     * @param beanClass          bean class
     * @param annotation         Annotation
     * @param method             listener method
     * @param applicationContext {@link ApplicationContext}
     */
    protected abstract void processListenerMethod(String beanName, Object bean, Class<?> beanClass, T annotation, Method method, ApplicationContext applicationContext);


}
