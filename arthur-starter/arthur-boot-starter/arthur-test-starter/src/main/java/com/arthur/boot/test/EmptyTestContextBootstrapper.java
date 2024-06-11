package com.arthur.boot.test;

import org.springframework.boot.test.context.SpringBootTestContextBootstrapper;
import org.springframework.core.annotation.AnnotatedElementUtils;

/**
 * Spring Boot测试上下文信息
 *
 * @author DearYang
 * @date 2022-08-02
 * @since 1.0
 */
public class EmptyTestContextBootstrapper extends SpringBootTestContextBootstrapper {

	@Override
	protected String[] getProperties(Class<?> testClass) {
		TestEmptyEnvironment annotation = AnnotatedElementUtils.getMergedAnnotation(testClass, TestEmptyEnvironment.class);
		return annotation != null ? annotation.properties() : null;
	}

}
