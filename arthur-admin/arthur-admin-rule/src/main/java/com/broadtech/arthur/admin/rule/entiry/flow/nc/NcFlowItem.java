package com.broadtech.arthur.admin.rule.entiry.flow.nc;

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
public class NcFlowItem {
    private Integer index;

    /**
     * Strategy for parsing item (e.g. client IP, arbitrary headers and URL parameters).
     */
    private Integer parseStrategy;
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
    private Integer matchStrategy = SentinelGatewayConstants.PARAM_MATCH_STRATEGY_EXACT;
}
