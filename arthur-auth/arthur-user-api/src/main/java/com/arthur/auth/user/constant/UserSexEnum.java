package com.arthur.auth.user.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 性别枚举
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-04-03
 * @since 1.0
 */
@Getter
@RequiredArgsConstructor
public enum UserSexEnum {

	/**
	 * 男
	 */
	MALE(1),

	/**
	 * 女
	 */
	FEMALE(2),

	/**
	 * 未知
	 */
	UNKNOWN(3),
	;

	final int code;

}
