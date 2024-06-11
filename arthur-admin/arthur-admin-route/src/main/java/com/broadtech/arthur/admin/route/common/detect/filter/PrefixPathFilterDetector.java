package com.broadtech.arthur.admin.route.common.detect.filter;

import cn.hutool.core.util.StrUtil;
import com.broadtech.arthur.admin.common.detect.RouteDetector;
import com.broadtech.arthur.admin.route.entity.nc.GatewayRouteDefinition;
import com.broadtech.arthur.common.utils.MapUtils;
import com.google.common.base.Preconditions;

import java.util.Map;

/**
 * @author Machenike
 * date 2022/10/31
 * @version 1.0.0
 */
public class PrefixPathFilterDetector extends RouteDetector<GatewayRouteDefinition.GatewayFilterDefinition> {
    private final String name   = "PrefixPath";
    private final String prefix = "prefix";

    @Override
    protected boolean support(GatewayRouteDefinition.GatewayFilterDefinition gatewayFilterDefinition) {
        return super.support(gatewayFilterDefinition)&&checkName(gatewayFilterDefinition.getName());
    }

    @Override
    public boolean checkName(String name) {
        return this.name.equals(name);
    }

    @Override
    public boolean checkArgs(GatewayRouteDefinition.GatewayFilterDefinition gatewayFilterDefinition) {
        Map<String, String> filterDefinitionArgs = gatewayFilterDefinition.getArgs();
        Preconditions.checkArgument(!MapUtils.isEmpty(filterDefinitionArgs)
                , new RuntimeException(StrUtil.format("PathPredicateDetector : path参数异常={}",filterDefinitionArgs)));
        Preconditions.checkArgument(!StrUtil.isEmpty(filterDefinitionArgs.get(prefix))
                ,new RuntimeException(StrUtil.format("PathPredicateDetector : path异常={}",filterDefinitionArgs)));
        return true;
    }

    @Override
    protected void doDetection(GatewayRouteDefinition.GatewayFilterDefinition gatewayFilterDefinition) {
        checkArgs(gatewayFilterDefinition);
    }
}
