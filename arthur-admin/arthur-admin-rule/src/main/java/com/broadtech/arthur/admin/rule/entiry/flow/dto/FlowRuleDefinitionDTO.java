package com.broadtech.arthur.admin.rule.entiry.flow.dto;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.broadtech.arthur.admin.rule.entiry.api.Constant.SentinelGatewayConstants;
import com.broadtech.arthur.admin.rule.entiry.flow.nc.NcFlowItem;
import com.broadtech.arthur.admin.rule.entiry.flow.nc.NcFlowRule;
import com.broadtech.arthur.admin.rule.entiry.flow.po.FlowRule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;


/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/8/15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlowRuleDefinitionDTO {

    private String id;
    private String groupId;

    private String resource;
    private Integer resourceMode = SentinelGatewayConstants.RESOURCE_MODE_ROUTE_ID;

    private Integer grade = RuleConstant.FLOW_GRADE_QPS;
    private Double count;
    private Long intervalSec = 1L;

    private Integer controlBehavior = RuleConstant.CONTROL_BEHAVIOR_DEFAULT;
    private Integer burst;
    /**
     * For throttle (rate limiting with queueing).
     */
    private Integer maxQueueingTimeoutMs = 500;
    /**
     * Should be set when applying to parameter flow rules.
     */
    private Integer indexes;
    /**
     * Strategy for parsing item (e.g. client IP, arbitrary headers and URL parameters).
     */
    private int parseStrategy;
    /**
     * Field to get (only required for arbitrary headers or URL parameters mode).
     */
    private String fieldName;
    /**
     * Matching pattern. If not set, all values will be kept in LRU map.
     */
    private String pattern;
    /**
     * Matching strategy for item value.
     */
    private int matchStrategy = SentinelGatewayConstants.PARAM_MATCH_STRATEGY_EXACT;

    public FlowRule dto2po() {
        FlowRule flowRule = new FlowRule();
        BeanUtils.copyProperties(this, flowRule);
        return new FlowRule();
    }

    public NcFlowRule dto2nc() {
        NcFlowRule ncFlowRule = new NcFlowRule();
        NcFlowItem ncFlowItem = new NcFlowItem();
        BeanUtils.copyProperties(this, ncFlowRule);
        BeanUtils.copyProperties(this, ncFlowItem);
        ncFlowRule.setParamItem(ncFlowItem);
        return ncFlowRule;
    }


}
