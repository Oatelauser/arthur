package com.broadtech.arthur.admin.resource.security;

import com.broadtech.arthur.admin.resource.security.impl.ReactiveAuthorizationServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorityAuthorizationDecision;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.function.Function;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/9/15
 */
@Slf4j
@Component
public class JwtAuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    AuthorizationService authorizationService;

    @Autowired
    public void setAuthorizationService(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext ctx) {

        return authentication
                .filter(Authentication::isAuthenticated)
                .flatMapIterable(Authentication::getAuthorities)
                .map(GrantedAuthority::getAuthority)
                .any(auth -> authorizationService.verifyPermissions(ctx, auth))
                .map(AuthorizationDecision::new)
                .defaultIfEmpty(new AuthorizationDecision(false));

    }
}
