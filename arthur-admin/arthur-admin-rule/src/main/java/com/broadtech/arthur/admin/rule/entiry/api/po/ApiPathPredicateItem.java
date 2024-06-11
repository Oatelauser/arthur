package com.broadtech.arthur.admin.rule.entiry.api.po;

import com.broadtech.arthur.admin.rule.entiry.api.Constant.SentinelGatewayConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/8/15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiPathPredicateItem  {
    private String pattern;
    private int matchStrategy = SentinelGatewayConstants.URL_MATCH_STRATEGY_EXACT;
}
