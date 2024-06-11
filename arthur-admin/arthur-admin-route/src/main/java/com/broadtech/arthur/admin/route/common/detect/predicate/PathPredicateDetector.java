package com.broadtech.arthur.admin.route.common.detect.predicate;

import cn.hutool.core.util.StrUtil;
import com.broadtech.arthur.admin.common.detect.RouteDetector;
import com.broadtech.arthur.admin.route.entity.nc.GatewayRouteDefinition;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author Machenike
 * @Date 2022/7/19
 * @Version 1.0.0
 * @Description
 */
@Component("Path")
public class PathPredicateDetector extends RouteDetector<GatewayRouteDefinition.GatewayPredicateDefinition> {
    private final String name               = "Path";
    private final String patterns           = "patterns";

    @Override
    public boolean support(GatewayRouteDefinition.GatewayPredicateDefinition gatewayPredicateDefinition) {
        return super.support(gatewayPredicateDefinition) && checkName(gatewayPredicateDefinition.getName());
    }

    @Override
    public boolean checkArgs(GatewayRouteDefinition.GatewayPredicateDefinition gatewayPredicateDefinition) {
        Map<String, String> args = gatewayPredicateDefinition.getArgs();
        if (StrUtil.isEmpty(args.get(patterns))) {
            throw new RuntimeException(StrUtil.format("PathPredicateDetector : path异常end={}",args.get(patterns)));
        }
        String flag = "false";
        String matchTrailingSlash = "matchTrailingSlash";
        if (StrUtil.isEmpty(args.get(matchTrailingSlash)) || !flag.equals(args.get(matchTrailingSlash))) {
            throw new RuntimeException(StrUtil.format("PathPredicateDetector : path异常end={}",args.get(patterns)));
        }
        return true;
    }

    @Override
    protected void doDetection(GatewayRouteDefinition.GatewayPredicateDefinition gatewayPredicateDefinition) {
        checkName(gatewayPredicateDefinition.getName());
        checkArgs(gatewayPredicateDefinition);
    }

    public boolean checkName(String name) {
        return this.name.equals(name);
    }


}
