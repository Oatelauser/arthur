package com.broadtech.arthur.admin.route.aop;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.broadtech.arthur.admin.common.annotation.CheckRoute;
import com.broadtech.arthur.admin.common.detect.RouteDetectorChain;
import com.broadtech.arthur.admin.route.entity.dto.RouteDefinitionDTO;
import com.broadtech.arthur.admin.route.entity.nc.GatewayRouteDefinition;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/9/22
 */
@Aspect
@Component
public class RouteCheckAspect {

    private RouteDetectorChain chain;

    @Autowired
    public void setChain(RouteDetectorChain chain) {
        this.chain = chain;
    }

    @Pointcut("execution(* com.broadtech.arthur.admin.route.service.impl.RouteBusinessServiceImpl.*( .. ))")
    void point() {
    }

//    @Before("point()")
    public void checkRoute(ProceedingJoinPoint joinPoint) {

        Signature signature = joinPoint.getSignature();

        if (signature==null) {
            return;
        }

        MethodSignature methodSignature = (MethodSignature) signature;

        Method method = methodSignature.getMethod();

        CheckRoute checkRoute = method.getAnnotation(CheckRoute.class);

        if (ObjectUtil.isEmpty(checkRoute)) {
            return;
        }

        Object[] args = joinPoint.getArgs();
        if (ArrayUtil.isEmpty(args)) {
            return;
        }

        Object o = args[0];
        if (!(o instanceof RouteDefinitionDTO)) {
            return;
        }

        RouteDefinitionDTO routeDefinitionDTO = (RouteDefinitionDTO) o;
        GatewayRouteDefinition gatewayRouteDefinition = GatewayRouteDefinition.assembleRouteDefinition(routeDefinitionDTO);
        gatewayRouteDefinition.getFilters().forEach(chain::detection);
        gatewayRouteDefinition.getPredicates().forEach(chain::detection);

    }


}
