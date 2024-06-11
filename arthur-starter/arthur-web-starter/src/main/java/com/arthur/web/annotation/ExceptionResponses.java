package com.arthur.web.annotation;

import java.lang.annotation.*;

/**
 * {@link ExceptionResponse}注解容器
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-04-18
 * @since 1.0
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface ExceptionResponses {

	ExceptionResponse[] value() default {};

}
