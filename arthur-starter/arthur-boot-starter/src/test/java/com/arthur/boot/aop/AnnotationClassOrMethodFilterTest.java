package com.arthur.boot.aop;

import org.junit.jupiter.api.Test;
import org.springframework.util.ReflectionUtils;

class AnnotationClassOrMethodFilterTest {

	@Test
	public void t1() {
		ReflectionUtils.doWithLocalFields(T1.class, field -> {
			System.out.println(field.getName());
		});
	}

	static class T {

		private static String age1;
		private String name;

	}

	static class T1 extends T {

		private static String age1;
		private String name1;

	}


}
