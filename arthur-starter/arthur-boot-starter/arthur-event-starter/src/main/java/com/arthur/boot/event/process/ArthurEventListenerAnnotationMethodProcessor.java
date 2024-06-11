package com.arthur.boot.event.process;

import com.arthur.boot.event.annotation.ArthurEventListener;
import com.arthur.boot.process.AnnotationListenerMethodPostProcessor;
import com.arthur.boot.process.AnnotationMethodProcessor;
import com.arthur.common.notify.event.Event;
import com.arthur.common.notify.publisher.EventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.ResolvableType;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * 处理{@link ArthurEventListener}的bean对象
 *
 * @author DearYang
 * @date 2022-08-29
 * @see AnnotationMethodProcessor
 * @see AnnotationListenerMethodPostProcessor
 * @since 1.0
 */
public class ArthurEventListenerAnnotationMethodProcessor implements AnnotationMethodProcessor<ArthurEventListener>, ApplicationContextAware {

    private static final Logger LOG = LoggerFactory.getLogger(ArthurEventListenerAnnotationMethodProcessor.class);

    private EventPublisher eventPublisher;
    private ApplicationContext applicationContext;

    @Override
    public Class<ArthurEventListener> getAnnotationType() {
        return ArthurEventListener.class;
    }

    @Override
    public boolean supports(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length != 1) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Listener method [" + method + "] parameters count must be one");
            }
            return false;
        }

        ResolvableType declaredType = ResolvableType.forMethodParameter(method, 0);
        if (!Event.class.isAssignableFrom(declaredType.toClass())) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("Listener method [" + method + "] parameter type must be [" + Event.class + "]");
                return false;
            }
        }
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void processMethod(String beanName, Class<?> targetType, Method annotatedMethod) {
        Method method = BridgeMethodResolver.findBridgedMethod(annotatedMethod);
        //Method targetMethod = Proxy.isProxyClass(targetType) ? method : AopUtils.getMostSpecificMethod(annotatedMethod, targetType);
        //ArthurEventListener annotation = AnnotatedElementUtils.findMergedAnnotation(targetMethod, getAnnotationType());
        ResolvableType declaredType = ResolvableType.forMethodParameter(annotatedMethod, 0);
        Object bean = getTargetBean(beanName);
        eventPublisher.addSubscriber((Class<? extends Event>) declaredType.toClass(), event -> doInvoke(method, bean, event));
    }

    public void doInvoke(Method method, Object bean, Object... args) {
        if (bean == null) {
            return;
        }

        ReflectionUtils.makeAccessible(method);
        try {
            method.invoke(bean, args);
        } catch (IllegalArgumentException ex) {
            assertTargetBean(method, bean, args);
            throw new IllegalStateException(getInvocationErrorMessage(method, bean, ex.getMessage(), args), ex);
        } catch (IllegalAccessException ex) {
            throw new IllegalStateException(getInvocationErrorMessage(method, bean, ex.getMessage(), args), ex);
        } catch (InvocationTargetException ex) {
            // Throw underlying exception
            Throwable targetException = ex.getTargetException();
            if (targetException instanceof RuntimeException) {
                throw (RuntimeException) targetException;
            } else {
                String msg = getInvocationErrorMessage(method, bean, "Failed to invoke event listener method", args);
                throw new UndeclaredThrowableException(targetException, msg);
            }
        }
    }

    protected Object getTargetBean(String beanName) {
        Assert.notNull(this.applicationContext, "ApplicationContext must no be null");
        return this.applicationContext.getBean(beanName);
    }

    private String getInvocationErrorMessage(Method method, Object bean, String message, Object[] resolvedArgs) {
        StringBuilder sb = new StringBuilder(getDetailedErrorMessage(method, bean, message));
        sb.append("Resolved arguments: \n");
        for (int i = 0; i < resolvedArgs.length; i++) {
            sb.append('[').append(i).append("] ");
            if (resolvedArgs[i] == null) {
                sb.append("[null] \n");
            } else {
                sb.append("[type=").append(resolvedArgs[i].getClass().getName()).append("] ");
                sb.append("[value=").append(resolvedArgs[i]).append("]\n");
            }
        }
        return sb.toString();
    }

    protected String getDetailedErrorMessage(Method method, Object bean, String message) {
        return message + '\n' +
                "HandlerMethod details: \n" +
                "Bean [" + bean.getClass().getName() + "]\n" +
                "Method [" + method.toGenericString() + "]\n";
    }

    private void assertTargetBean(Method method, Object targetBean, Object[] args) {
        Class<?> methodDeclaringClass = method.getDeclaringClass();
        Class<?> targetBeanClass = targetBean.getClass();
        if (!methodDeclaringClass.isAssignableFrom(targetBeanClass)) {
            String msg = "The event listener method class '" + methodDeclaringClass.getName() +
                    "' is not an instance of the actual bean class '" +
                    targetBeanClass.getName() + "'. If the bean requires proxying " +
                    "(e.g. due to @Transactional), please use class-based proxying.";
            throw new IllegalStateException(getInvocationErrorMessage(method, targetBean, msg, args));
        }
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        this.eventPublisher = applicationContext.getBean(EventPublisher.class);
    }

}
