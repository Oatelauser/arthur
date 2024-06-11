package com.broadtech.arthur.admin.resource.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/9/15
 */
public interface AuthorizationService {


    /**
     * 验证权限
     *
     * @param ctx        ctx
     * @param permission 许可
     * @return boolean
     */
    boolean verifyPermissions(AuthorizationContext ctx,String permission);



}
