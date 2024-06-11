package com.broadtech.arthur.admin.rule.entiry.api.dto;

import com.alibaba.fastjson2.JSON;
import com.broadtech.arthur.admin.rule.entiry.api.nc.NcApiRule;
import com.broadtech.arthur.admin.rule.entiry.api.po.ApiRule;
import com.broadtech.arthur.admin.rule.entiry.api.po.ApiPredicateGroupItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/8/15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiDefinitionDTO {


    @NotNull
    private String id;
    @NotNull
    private String groupId;

    @NotNull
    private String apiName;
    @NotNull
    private Set<ApiPredicateGroupItem> predicateItems;

    public ApiDefinitionDTO(String apiName) {
        this.apiName = apiName;
    }

    public static ApiDefinitionDTO assembleApiDefinition(ApiRule apiRule) {

        String apiStr = JSON.toJSONString(apiRule);
        ApiDefinitionDTO dto = JSON.parseObject(apiStr, ApiDefinitionDTO.class);
        return dto;
    }


    public ApiRule dto2po() {
        ApiRule apiRule = new ApiRule();
        BeanUtils.copyProperties(this,apiRule);
        return apiRule;
    }

    public NcApiRule dto2nc() {
        NcApiRule ncApiRule = new NcApiRule();
        BeanUtils.copyProperties(this, ncApiRule);
        return ncApiRule;
    }
}
