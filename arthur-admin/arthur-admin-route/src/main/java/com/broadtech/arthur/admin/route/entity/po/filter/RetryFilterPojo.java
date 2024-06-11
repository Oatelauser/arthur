package com.broadtech.arthur.admin.route.entity.po.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

/**
 * @Author Machenike
 * @Date 2022/7/19
 * @Version 1.0.0
 * @Description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RetryFilterPojo {
    private final String name = "Retry";
    private String routeId;
    private int retries=3;
    private BackoffConfig backoff;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BackoffConfig {

        private Duration firstBackoff = Duration.ofMillis(5);

        private Duration maxBackoff;

        private int factor = 2;

        private boolean basedOnPreviousValue = true;
    }
    }
