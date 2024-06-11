package com.arthur.oauth2.annotation;

import com.arthur.oauth2.authentication.OAuth2AnnotationAnalysisProcessor;

import java.lang.annotation.*;

/**
 * 认证注解
 * <p>
 * 该注解用于controller中，用于状态标识
 *
 * @author DearYang
 * @date 2022-08-14
 * @see OAuth2AnnotationAnalysisProcessor
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface AuthPermission {

    /**
     * URL权限级别
     *
     * @return {@link Permission}
     */
    Permission permission() default Permission.INTERNAL;

    enum Permission {

        /**
         * 内部URL
         */
        INTERNAL,

        /**
         * 对外暴露URL
         */
        EXPOSE,

    }

}
