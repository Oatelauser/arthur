package com.arthur.web.servlet.version;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 开启API请求版本
 *
 * @author DearYang
 * @date 2022-10-13
 * @since 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(WebMvcRegistrationsConfiguration.class)
public @interface EnableRequestMappingVersion {

    String value() default "";

}
