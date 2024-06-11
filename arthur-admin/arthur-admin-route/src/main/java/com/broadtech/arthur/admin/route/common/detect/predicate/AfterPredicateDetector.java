package com.broadtech.arthur.admin.route.common.detect.predicate;

import cn.hutool.core.util.StrUtil;
import com.broadtech.arthur.admin.common.detect.RouteDetector;
import com.broadtech.arthur.admin.route.entity.nc.GatewayRouteDefinition;
import lombok.extern.slf4j.Slf4j;

import java.time.ZonedDateTime;
import java.util.Map;

/**
 * @author Machenike
 * date 2022/10/31
 * @version 1.0.0
 */
@Slf4j
public class AfterPredicateDetector extends RouteDetector<GatewayRouteDefinition.GatewayPredicateDefinition> {

    private final String name     = "After";
    private final String datetime = "datetime";

    @Override
    protected boolean support(GatewayRouteDefinition.GatewayPredicateDefinition gatewayPredicateDefinition) {
        return super.support(gatewayPredicateDefinition) && checkName(gatewayPredicateDefinition.getName());
    }

    @Override
    protected void doDetection(GatewayRouteDefinition.GatewayPredicateDefinition gatewayPredicateDefinition) {
        checkArgs(gatewayPredicateDefinition);
    }

    @Override
    public boolean checkName(String name) {
        return this.name.equals(name);
    }

    @Override
    public boolean checkArgs(GatewayRouteDefinition.GatewayPredicateDefinition definition) {
        Map<String, String> args = definition.getArgs();
        if (StrUtil.isEmpty(args.get(datetime))) {
            throw new RuntimeException(StrUtil.format("AfterPredicateDetector : 时间格式异常datetime={}",args.get(datetime)));
        }
        try {
            ZonedDateTime.parse(args.get(datetime));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
        return true;
    }

}
