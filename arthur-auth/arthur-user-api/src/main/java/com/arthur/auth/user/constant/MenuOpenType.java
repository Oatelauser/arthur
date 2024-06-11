package com.arthur.auth.user.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 菜单打开方式
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-04-03
 * @since 1.0
 */
@Getter
@RequiredArgsConstructor
public enum MenuOpenType {

	/**
	 * 1-页签
	 */
	ITEM(1),

	/**
	 * 2-新窗口
	 */
	BLANK(2),
	;

	final int code;

}
