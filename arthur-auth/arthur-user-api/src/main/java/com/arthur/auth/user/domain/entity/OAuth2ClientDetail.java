package com.arthur.auth.user.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 终端信息表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value ="sys_oauth_client_details")
public class OAuth2ClientDetail extends BaseEntity implements Serializable {

	@Serial
	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/**
     * 客户端ID
     */
    @TableId(value = "client_id")
	@Schema(description = "客户端ID")
	@NotBlank(message = "clientId不能为空")
    private String clientId;

    /**
     * 资源列表
     */
	@Schema(description = "资源ID列表")
    @TableField(value = "resource_ids")
    private String resourceIds;

    /**
     * 客户端密钥
     */
	@Schema(description = "客户端密钥")
    @TableField(value = "client_secret")
	@NotBlank(message = "clientSecret不能为空")
    private String clientSecret;

    /**
     * 域
     */
    @TableField(value = "scope")
	@Schema(description = "作用域")
	@NotBlank(message = "scope不能为空")
    private String scope;

    /**
     * 认证类型
     */
	@Schema(description = "授权方式")
    @TableField(value = "authorized_grant_types")
    private String grantTypes;

    /**
     * 重定向地址
     */
	@Schema(description = "回调地址")
    @TableField(value = "web_server_redirect_uri")
    private String redirectUri;

    /**
     * 角色列表
     */
	@Schema(description = "权限列表")
    @TableField(value = "authorities")
    private String authorities;

    /**
     * 请求令牌有效时间
     */
	@Schema(description = "请求令牌有效时间")
    @TableField(value = "token_live_time")
    private String tokenLiveTime;

    /**
     * 刷新令牌有效期
     */
	@Schema(description = "刷新令牌的有效时间")
    @TableField(value = "refresh_token_live_time")
    private String refreshTokenLiveTime;

    /**
     * 令牌扩展字段JSON
     */
	@Schema(description = "令牌拓展信息")
    @TableField(value = "additional_information")
    private String additionalInformation;

    /**
     * 是否自动放行
     */
	@Schema(description = "是否自动放行: 0-不自动放行 1-自动放行")
    @TableField(value = "auto_approve")
    private Boolean autoApprove;

}
