package com.arthur.oauth2.annotation;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.AccessDeniedException;

import static com.arthur.auth.user.constant.InternalAuthConstants.INTERNAL_AUTH_HEADER_NAME;
import static com.arthur.auth.user.constant.InternalAuthConstants.INTERNAL_AUTH_HEADER_VALUE;
import static com.arthur.oauth2.annotation.AuthPermission.Permission.EXPOSE;


/**
 * OAuth2权限注解方法拦截器AOP
 *
 * @author DearYang
 * @date 2022-08-16
 * @see AuthPermission
 * @since 1.0
 */
public class AuthPermissionAdvice implements MethodInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(AuthPermissionAdvice.class);

    private final HttpServletRequest request;

    public AuthPermissionAdvice(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    @Nullable
    @SuppressWarnings("all")
    public Object invoke(@NonNull MethodInvocation invocation) throws Throwable {
        AuthPermission annotation = AnnotationUtils.findAnnotation(invocation.getMethod(), AuthPermission.class);
        if (annotation == null) {
            // 不可能出现空指针异常
            Class<?> origin = invocation.getThis().getClass();
            annotation = AnnotationUtils.findAnnotation(origin, AuthPermission.class);
        }

        // 对外暴露的接口直接放行
        if (EXPOSE.equals(annotation.permission())) {
            return invocation.proceed();
        }

        if (!INTERNAL_AUTH_HEADER_VALUE.equals(request.getHeader(INTERNAL_AUTH_HEADER_NAME))) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("access interface method [{}], Permission is denied", invocation.getMethod().getName());
            }
            throw new AccessDeniedException("Access is denied");
        }

        return invocation.proceed();
    }

}
