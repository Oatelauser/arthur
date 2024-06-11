package com.broadtech.arthur.admin.rule.entiry.api.nc;

import com.broadtech.arthur.admin.rule.entiry.api.dto.ApiDefinitionDTO;
import com.broadtech.arthur.admin.rule.entiry.api.po.ApiPredicateGroupItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/8/22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NcApiRule {
    private String id;
    private String apiName;
    private Set<ApiPredicateGroupItem> predicateItems;


}
