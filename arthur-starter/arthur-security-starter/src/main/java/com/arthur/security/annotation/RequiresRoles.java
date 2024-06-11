package com.arthur.security.annotation;

import java.lang.annotation.*;

/**
 * 权限校验用户角色
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-05-08
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface RequiresRoles {

	/**
	 * 需要的角色，默认不需要角色
	 *
	 * @return 角色
	 */
	String[] roles() default {};

}
