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
@Component("Before")
@Slf4j
public class BeforePredicateDetector extends RouteDetector<GatewayRouteDefinition.GatewayPredicateDefinition> {
    private String name   = "Before";
    private String before = "datetime";


    @Override
    public boolean support(GatewayRouteDefinition.GatewayPredicateDefinition gatewayPredicateDefinition) {
        return super.support(gatewayPredicateDefinition) && checkName(gatewayPredicateDefinition.getName());
    }

    @Override
    public boolean checkArgs(GatewayRouteDefinition.GatewayPredicateDefinition gatewayPredicateDefinition) {
        Map<String, String> args = gatewayPredicateDefinition.getArgs();
        if (StrUtil.isEmpty(args.get(before))) {
            throw new RuntimeException(StrUtil.format("AfterPredicateDetector : 时间格式异常datetime={}",args.get(before)));
        }
        try {
            ZonedDateTime.parse(args.get(before));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    protected void doDetection(GatewayRouteDefinition.GatewayPredicateDefinition gatewayPredicateDefinition) {
        checkName(gatewayPredicateDefinition.getName());
        checkArgs(gatewayPredicateDefinition);
    }

    @Override
    public boolean checkName(String name) {
        return this.name.equals(name) ;
    }


}
