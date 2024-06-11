package com.arthur.web.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.*;

/**
 * 用于模拟{@link org.springframework.web.bind.annotation.RequestMapping}注解的参数，防止注册多个URL
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-04-20
 * @see org.springframework.web.bind.annotation.RequestMapping
 * @since 1.0
 */
@Mapping
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD})
public @interface SpringMapping {

    /**
     * @see RequestMapping#name()
     */
    String name() default "";

    /**
     * @see RequestMapping#value()
     */
    @AliasFor("path")
    String[] value() default {};

    /**
     * @see RequestMapping#path()
     */
    @AliasFor("value")
    String[] path() default {};

    /**
     * @see RequestMapping#method()
     */
    RequestMethod[] method() default {};

    /**
     * @see RequestMapping#params()
     */
    String[] params() default {};

    /**
     * @see RequestMapping#headers()
     */
    String[] headers() default {};

    /**
     * @see RequestMapping#consumes()
     */
    String[] consumes() default {};

    /**
     * @see RequestMapping#produces()
     */
    String[] produces() default {};

}
