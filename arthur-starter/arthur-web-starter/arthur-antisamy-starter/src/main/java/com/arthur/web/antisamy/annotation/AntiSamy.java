package com.arthur.web.antisamy.annotation;

import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;


/**
 * 标记controller及其方法的AntiSamy防御请求
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-06
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface AntiSamy {

	/**
	 * 防御的请求头
	 */
	String[] defenseHeaders() default "*";

	/**
	 * 防御的cookies
	 */
	String[] defenseCookies() default "*";

	/**
	 * 防御请求内容
	 */
	boolean defenseQueryBody() default true;

	/**
	 * 允许的请求方法
	 */
	RequestMethod[] method() default { POST, PUT };

}
