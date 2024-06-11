package com.arthur.web.annotation;

import java.lang.annotation.*;

/**
 * 接口返回的默认异常响应
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-04-07
 * @since 1.0
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(ExceptionResponses.class)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface ExceptionResponse {

    /**
     * 处理的兜底异常
     */
    Class<? extends Throwable>[] value() default Exception.class;

    /**
     * 响应code
     */
    String code();

    /**
     * 响应异常信息
     */
    String msg() default "";

    /**
     * 是否把异常信息作为msg
     */
    boolean showException() default false;

}
