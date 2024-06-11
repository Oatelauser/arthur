package com.arthur.web.servlet.constant;

import com.arthur.web.annotation.SpringMapping;
import com.arthur.web.servlet.process.SpringMappingHandlerRegistar;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 注解扫描策略
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-04-25
 * @see SpringMappingHandlerRegistar
 * @since 1.0
 */
public enum SearchStrategy {

	/**
	 * 只取{@link RequestMapping}注解
	 */
	REQUEST_MAPPING,

	/**
	 * 只取{@link SpringMapping}注解
	 */
	SPRING_MAPPING,

	/**
	 * 两个注解都取
	 */
	ALL,

}
