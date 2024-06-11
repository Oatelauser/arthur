package com.arthur.auth.user.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 基础DAO
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-04-03
 * @since 1.0
 */
@Data
public class BaseEntity {

	/**
	 * 创建时间
	 */
	@TableField(value = "create_time")
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@TableField(value = "update_time")
	private LocalDateTime updateTime;

}
