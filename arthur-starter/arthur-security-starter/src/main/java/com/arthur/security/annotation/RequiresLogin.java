package com.arthur.security.annotation;

import java.lang.annotation.*;

/**
 * 权限校验用户必须登录
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-05-08
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface RequiresLogin {
}
