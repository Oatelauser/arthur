package com.arthur.boot.mybatis.annotation;

import com.arthur.boot.mybatis.datasource.DatabaseAnnotationAdvisor;
import com.arthur.boot.mybatis.datasource.DatabaseConfigurationSelector;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 开启多数据源注解
 *
 * @author DearYang
 * @date 2022-09-23
 * @see DatabaseAnnotationAdvisor
 * @since 1.0
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(DatabaseConfigurationSelector.class)
public @interface EnableMultiDatabase {

	@AliasFor(value = "annotation")
	Class<? extends Annotation> value() default Annotation.class;

	@AliasFor(value = "value")
	Class<? extends Annotation> annotation() default Annotation.class;

	boolean proxyTargetClass() default false;

	AdviceMode mode() default AdviceMode.PROXY;

	int order() default Ordered.LOWEST_PRECEDENCE;

}
