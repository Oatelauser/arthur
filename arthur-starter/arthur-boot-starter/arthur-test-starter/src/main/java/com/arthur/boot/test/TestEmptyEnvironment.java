package com.arthur.boot.test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.autoconfigure.filter.TypeExcludeFilters;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.annotation.*;

/**
 * SpringBoot测试注解
 * <p>
 * 该注解默认不会加载任何Bean对象，以最轻量化启动测试任务
 * 如果需要添加Bean对象，请使用注解自己导入，自定义加载配置:
 * 1.{@link org.springframework.context.annotation.Import}
 * 2.{@link org.springframework.boot.autoconfigure.ImportAutoConfiguration}
 *
 * @author DearYang
 * @date 2022-08-02
 * @see EmptyTestContextBootstrapper
 * @see EmptyTypeExcludeFilter
 * @since 1.0
 */
@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(SpringExtension.class)
@OverrideAutoConfiguration(enabled = false)
@TypeExcludeFilters(EmptyTypeExcludeFilter.class)
@BootstrapWith(EmptyTestContextBootstrapper.class)
public @interface TestEmptyEnvironment {

	/***
	 * 加载的测试配置文件
	 *
	 * @return 文件路径
	 */
	String[] properties() default {};

	/**
	 * 是否使用默认的过滤器
	 *
	 * @return yes or no
	 */
	boolean useDefaultFilters() default true;

	/**
	 * 包含的加载过滤器
	 *
	 * @return {@link ComponentScan.Filter}
	 */
	ComponentScan.Filter[] includeFilters() default {};

	/**
	 * 排除的过滤器
	 *
	 * @return {@link ComponentScan.Filter}
	 */
	ComponentScan.Filter[] excludeFilters() default {};

}
