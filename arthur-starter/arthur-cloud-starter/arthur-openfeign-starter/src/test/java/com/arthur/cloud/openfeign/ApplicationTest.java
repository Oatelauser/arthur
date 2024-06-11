package com.arthur.cloud.openfeign;

import com.arthur.cloud.openfeign.autoconfigure.OpenFeignAutoConfiguration;
import com.arthur.boot.test.TestEmptyEnvironment;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;

/**
 * 概要描述
 * <p>
 * 详细描述
 *
 * @author DearYang
 * @date 2022-09-07
 * @since 1.0
 */
@SpringBootConfiguration
@TestEmptyEnvironment
@ImportAutoConfiguration(OpenFeignAutoConfiguration.class)
public class ApplicationTest {

	@Test
	public void t1() {
		System.out.println(666);
	}

}
