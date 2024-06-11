package com.broadtech.arthur.admin.route.config;

import com.broadtech.arthur.admin.route.common.detect.filter.PrefixPathFilterDetector;
import com.broadtech.arthur.admin.route.common.detect.predicate.AfterPredicateDetector;
import com.broadtech.arthur.admin.common.detect.RouteDetectorChain;
import com.broadtech.arthur.admin.route.common.detect.predicate.PathPredicateDetector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Machenike
 * date 2022/10/31
 * @version 1.0.0
 */
@Configuration
public class DetectorConfig {

    @Bean
    public RouteDetectorChain getRouteDetectorChain() {
        RouteDetectorChain routeDetectorChain = new RouteDetectorChain();
        routeDetectorChain.addNext(new AfterPredicateDetector());
        routeDetectorChain.addNext(new PathPredicateDetector());
        routeDetectorChain.addNext(new PrefixPathFilterDetector());
        return routeDetectorChain;
    }


}
