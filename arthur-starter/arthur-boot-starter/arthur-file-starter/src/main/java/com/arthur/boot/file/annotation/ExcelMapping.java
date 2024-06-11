package com.arthur.boot.file.annotation;

import com.arthur.boot.file.handler.ExcelMappingHandlerMapping;
import com.arthur.web.annotation.SpringMapping;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.PARAMETER;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Excel模板下载请求映射注解
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-04-20
 * @see ExcelMappingHandlerMapping
 * @since 1.0
 */
@Documented
@SpringMapping(method = GET)
@Retention(RetentionPolicy.RUNTIME)
@Target({ PARAMETER, ANNOTATION_TYPE })
public @interface ExcelMapping {

    @AliasFor(annotation = SpringMapping.class)
    String name() default "";

    /**
     * Alias for {@link SpringMapping#value}.
     */
    @AliasFor(annotation = SpringMapping.class)
    String[] value() default "/downloadTemplate";

    /**
     * Alias for {@link SpringMapping#path}.
     */
    @AliasFor(annotation = SpringMapping.class)
    String[] path() default "/downloadTemplate";

    /**
     * Alias for {@link SpringMapping#params}.
     */
    @AliasFor(annotation = SpringMapping.class)
    String[] params() default {};

    /**
     * Alias for {@link SpringMapping#headers}.
     */
    @AliasFor(annotation = SpringMapping.class)
    String[] headers() default {};

    /**
     * Alias for {@link SpringMapping#consumes}.
     */
    @AliasFor(annotation = SpringMapping.class)
    String[] consumes() default {};

    /**
     * Alias for {@link SpringMapping#produces}.
     */
    @AliasFor(annotation = SpringMapping.class)
    String[] produces() default {};

}
