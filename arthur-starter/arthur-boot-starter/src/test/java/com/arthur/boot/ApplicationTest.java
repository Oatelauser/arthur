package com.arthur.boot;

import com.arthur.boot.autoconfigure.ArthurBootAutoConfiguration;
import com.arthur.boot.aware.GenericAware;
import com.arthur.boot.aware.MyBean;
import com.arthur.boot.process.AwareFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * 概要描述
 * <p>
 * 详细描述
 *
 * @author DearYang
 * @date 2022-08-29
 * @since 1.0
 */
@SpringBootApplication
@ImportAutoConfiguration(ArthurBootAutoConfiguration.class)
public class ApplicationTest {

	public static void main(String[] args) {
		SpringApplication.run(ApplicationTest.class, args);
	}

	@Bean
	public AwareFactoryBean awareBean() {
		return new AwareFactoryBean(GenericAware.class);
	}

	@Bean
	public MyBean.MyBean2 myBean() {
		return new MyBean.MyBean2();
	}

}
