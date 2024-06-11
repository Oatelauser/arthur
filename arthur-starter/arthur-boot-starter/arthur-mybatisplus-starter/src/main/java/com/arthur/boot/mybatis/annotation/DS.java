package com.arthur.boot.mybatis.annotation;

import com.arthur.boot.mybatis.datasource.DatabaseAnnotationAdvisor;
import com.arthur.boot.mybatis.datasource.RouteLookup;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 数据源绑定注解
 *
 * @author DearYang
 * @date 2022-09-23
 * @see DatabaseAnnotationAdvisor
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface DS {

	/**
	 * @see DS#name()
	 */
	@AliasFor("name")
	String value() default "";

	/**
	 * 绑定数据源的唯一名称
	 *
	 * @return 数据源名称
	 */
	@AliasFor("value")
	String name() default "";

	/**
	 * {@link RouteLookup} Bean对象的名称
	 *
	 * @return bean对象名称
	 */
	String lookupName() default "";

}
