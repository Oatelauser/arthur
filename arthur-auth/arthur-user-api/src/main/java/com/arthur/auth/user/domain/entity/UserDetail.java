package com.arthur.auth.user.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户详细信息
 *
 * @author DearYang
 * @date 2022-09-05
 * @since 1.0
 */
@Data
public class UserDetail implements Serializable {

	@Serial
	private static final long serialVersionUID = -6240184383821196835L;

	@Schema(description = "主键")
	@TableId(value = "user_id", type = IdType.ASSIGN_ID)
	private Long userId;

	@Schema(description = "手机号")
	private String phone;

	@Schema(description = "用户名")
	private String username;

	@Schema(description = "密码")
	private String password;

	@Schema(description = "密码加盐")
	private transient String salt;

	@Schema(description = "所属部门ID")
	private Long deptId;

	@Schema(description = "锁定标记")
	private boolean locked;

	@TableLogic
	@Schema(description = "逻辑删除标记")
	private boolean deleted;

}
