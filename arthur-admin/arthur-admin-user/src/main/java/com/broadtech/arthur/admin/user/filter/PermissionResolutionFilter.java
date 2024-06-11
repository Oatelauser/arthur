package com.broadtech.arthur.admin.user.filter;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.TypeReference;
import com.broadtech.arthur.admin.common.execption.BaseException;
import com.broadtech.arthur.admin.user.entity.po.UserPO;
import com.broadtech.arthur.admin.user.service.PermissionService;
import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/9/9
 * @desc 微服务权限解析
 */
public class PermissionResolutionFilter extends OncePerRequestFilter implements PermissionService<HttpServletRequest> {

    private static final String USER = "user";
    private static final String PERMISSION = "permission";


    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest permissionInformation = checkTokenNotNull(request);
        populateTheContext(request);
        filterChain.doFilter(request, response);
    }


    @Override
    public <V> V checkTokenNotNull(HttpServletRequest request) {
        String user = request.getHeader(USER);
        Preconditions.checkNotNull(user, BaseException.NotFindTokenException.of());
        String permission = request.getHeader(PERMISSION);
        Preconditions.checkNotNull(permission, BaseException.NotFindTokenException.of());
        return (V) request;
    }


    @Override
    public void populateTheContext(HttpServletRequest request) {
        String user = request.getHeader(USER);
        String permission = request.getHeader(PERMISSION);
        UserPO userInfo = JSON.parseObject(user, UserPO.class);
        List<String> permissions = JSON.parseObject(permission, new TypeReference<List<String>>() {
        });
//        SecurityContext sc = SecurityContextHolder.getContext();
//        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(permissions.toArray(String[]::new));
//        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userInfo, null, authorities);
//        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//        sc.setAuthentication(authentication);
    }


}
