package com.arthur.auth.uaa.support;

import com.arthur.common.utils.TypeResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@link AuthenticationExceptionTranslator}实现类，根据不用类型的异常进行不同处理
 *
 * @author DearYang
 * @date 2022-08-20
 * @since 1.0
 */
@SuppressWarnings("unused")
public class DelegatingAuthenticationExceptionTranslator implements AuthenticationExceptionTranslator {

    private final AuthenticationExceptionTranslator defaultHandler;
    private final Map<Class<? extends AuthenticationException>, AuthenticationExceptionTranslator> handlers = new ConcurrentHashMap<>();

    public DelegatingAuthenticationExceptionTranslator(AuthenticationExceptionTranslator defaultHandler,
			Map<Class<? extends AuthenticationException>, AuthenticationExceptionTranslator> handlers) {
        this(defaultHandler);
        this.handlers.putAll(Objects.requireNonNull(handlers));
    }

    public DelegatingAuthenticationExceptionTranslator(AuthenticationExceptionTranslator defaultHandler,
			List<AuthenticationExceptionTranslator> handlers) {
        this(defaultHandler);
        register(handlers);
    }

    public DelegatingAuthenticationExceptionTranslator(AuthenticationExceptionTranslator defaultHandler) {
        this.defaultHandler = Objects.requireNonNull(defaultHandler);
    }

    @Override
    public AuthenticationException translateExceptionIfPossible(Authentication authentication,
			AuthenticationException exception) {
        for (Map.Entry<Class<? extends AuthenticationException>, AuthenticationExceptionTranslator> entry : handlers.entrySet()) {
            Class<? extends Exception> handlerMappedExceptionClass = entry.getKey();
            if (handlerMappedExceptionClass.isAssignableFrom(exception.getClass())) {
                AuthenticationExceptionTranslator handler = entry.getValue();
                return handler.translateExceptionIfPossible(authentication, exception);
            }
        }

        return defaultHandler.translateExceptionIfPossible(authentication, exception);
    }

    /**
     * 注册{@link AuthenticationExceptionTranslator}
     * <p>
     * 解析泛型具体类型在注册
     *
     * @param handlers {@link AuthenticationExceptionTranslator}
     */
    @SuppressWarnings("unchecked")
    public void register(List<AuthenticationExceptionTranslator> handlers) {
        if (CollectionUtils.isEmpty(handlers)) {
            return;
        }

        for (AuthenticationExceptionTranslator handler : handlers) {
            Class<?> candidate = AuthenticationException.class;
            if (handler instanceof AbstractAuthenticationExceptionTranslator) {
                Class<?>[] types = TypeResolver.resolveParamClasses(HandlerMethodResolver.ON_FAILURE_METHOD, handler.getClass());
                candidate = types[1];
            }
            if (AuthenticationException.class.isAssignableFrom(candidate)) {
                register((Class<? extends AuthenticationException>) candidate, handler);
            }
        }
    }

    /**
     * 根据k-v的形式注册
     *
     * @param type    {@link AuthenticationException}
     * @param handler {@link AuthenticationExceptionTranslator}
     */
    public void register(Class<? extends AuthenticationException> type, AuthenticationExceptionTranslator handler) {
        handlers.put(type, handler);
    }

    private static class HandlerMethodResolver {
        public static final Method ON_FAILURE_METHOD;

        static {
            try {
                ON_FAILURE_METHOD = AbstractAuthenticationExceptionTranslator.class.getMethod("translateException", Authentication.class, AuthenticationException.class);
            } catch (NoSuchMethodException e) {
                throw new IllegalStateException("no such method [doFailure]", e);
            }
        }
    }

}
