package com.arthur.auth.user.constant;

import com.arthur.auth.user.domain.entity.SysDept;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * {@link SysDept#getStatus()}状态枚举值
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-04-03
 * @since 1.0
 */
@Getter
@RequiredArgsConstructor
public enum TableStatusEnum {

	/**
	 * 0-删除
	 */
	DELETE(0),

	/**
	 * 1-停用
	 */
	DISABLED(1),

	/**
	 * 2-正常
	 */
	ENABLED(2),
	;

	final int code;

}
