package com.arthur.auth.upms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 用户中心启动类
 *
 * @author DearYang
 * @date 2022-09-05
 * @since 1.0
 */
@SpringBootApplication
@MapperScan("com.arthur.auth.upms.mapper")
public class ArthurUserApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArthurUserApplication.class, args);
	}

}
