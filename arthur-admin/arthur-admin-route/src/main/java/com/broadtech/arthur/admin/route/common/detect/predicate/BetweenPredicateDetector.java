package com.broadtech.arthur.admin.route.common.detect.predicate;

import cn.hutool.core.util.StrUtil;
import com.broadtech.arthur.admin.common.detect.RouteDetector;
import com.broadtech.arthur.admin.route.entity.nc.GatewayRouteDefinition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Map;

/**
 * @Author Machenike
 * @Date 2022/7/19
 * @Version 1.0.0
 * @Description
 */
@Component("Between")
@Slf4j
public class BetweenPredicateDetector extends RouteDetector<GatewayRouteDefinition.GatewayPredicateDefinition> {

    private final String name  = "Between";
    private final String start = "datetime1";
    private final String end   = "datetime2";

    @Override
    public boolean support(GatewayRouteDefinition.GatewayPredicateDefinition gatewayPredicateDefinition) {
        return super.support(gatewayPredicateDefinition) && checkName(gatewayPredicateDefinition.getName())&&checkName(gatewayPredicateDefinition.getName());
    }

    @Override
    public boolean checkArgs(GatewayRouteDefinition.GatewayPredicateDefinition gatewayPredicateDefinition) {
        Map<String, String> args = gatewayPredicateDefinition.getArgs();
        if (StrUtil.isEmpty(args.get(start))) {
            throw new RuntimeException(StrUtil.format("BetweenPredicateDetector : 时间格式异常start={}",args.get(start)));
        }
        if (StrUtil.isEmpty(args.get(end))) {
            throw new RuntimeException(StrUtil.format("BetweenPredicateDetector : 时间格式异常end={}",args.get(end)));
        }
        try {
            ZonedDateTime.parse(args.get(start));
            ZonedDateTime.parse(args.get(end));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    protected void doDetection(GatewayRouteDefinition.GatewayPredicateDefinition gatewayPredicateDefinition) {
        checkArgs(gatewayPredicateDefinition);
    }

    @Override
    public boolean checkName(String name) {
        return this.name.equals(name);
    }

}
