package com.broadtech.arthur.admin.auth.vo;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/9/16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
public class Oauth2TokenVo {
    /**
     * 访问令牌
     */
    private String token;
    /**
     * 刷新令牌
     */
    private String refreshToken;
    /**
     * 访问令牌头前缀
     */
    private String tokenHead;
    /**
     * 有效时间（秒）
     */
    private int expiresIn;
}
