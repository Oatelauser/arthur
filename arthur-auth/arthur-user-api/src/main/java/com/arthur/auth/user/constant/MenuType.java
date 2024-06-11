package com.arthur.auth.user.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 菜单类型
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-04-03
 * @since 1.0
 */
@Getter
@RequiredArgsConstructor
public enum MenuType {

	/**
	 * 1-目录
	 */
	DIR(1),

	/**
	 * 2-菜单
	 */
	MENU(2),

	/**
	 * 3-按钮
	 */
	BUTTON(3),
	;

	final int code;

}
