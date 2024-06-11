package com.arthur.security.annotation;

import com.arthur.security.constant.ActionType;

import java.lang.annotation.*;

/**
 * 权限校验用户资源的操作{@link ActionType}
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-05-08
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface RequiresPermissions {

	/**
	 * 操作权限，默认是读权限
	 *
	 * @return {@link ActionType}
	 */
	ActionType action() default ActionType.READ;

}
