package com.broadtech.arthur.admin.rule.entiry.flow.nc;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.broadtech.arthur.admin.rule.entiry.api.Constant.SentinelGatewayConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/8/22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NcFlowRule {
    private String resource;
    private int resourceMode = SentinelGatewayConstants.RESOURCE_MODE_ROUTE_ID;

    private int grade = RuleConstant.FLOW_GRADE_QPS;
    private double count;
    private long intervalSec = 1;

    private int controlBehavior = RuleConstant.CONTROL_BEHAVIOR_DEFAULT;
    private int burst;
    /**
     * For throttle (rate limiting with queueing).
     */
    private int maxQueueingTimeoutMs = 500;

    /**
     * For parameter flow control. If not set, the gateway rule will be
     * converted to normal flow rule.
     */
    private NcFlowItem paramItem;
}
