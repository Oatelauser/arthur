package com.broadtech.arthur.admin.resource.security.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson2.JSON;
import com.broadtech.arthur.admin.resource.security.AuthorizationService;
import com.broadtech.arthur.admin.resource.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/9/15
 */
@Component
public class ReactiveAuthorizationServiceImpl implements AuthorizationService {


    BaseService baseService;

    @Autowired
    public void setAuthService(BaseService authService) {
        this.baseService = authService;
    }

    @Override
    public boolean verifyPermissions(AuthorizationContext ctx, String permission) {
        return verifyAction(ctx,permission);
    }

    private boolean verifyAction(AuthorizationContext ctx,String permission) {
        String path = ctx.getExchange().getRequest().getURI().getPath();
        return baseService.verify(permission,path);
    }



}
