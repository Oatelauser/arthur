package com.broadtech.arthur.admin.rule.entiry.api.vo;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
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
public class ApiDefinitionVo {


    @NotNull
    private String id;
    @NotNull
    private String groupId;
    @NotNull
    private String apiName;
    private Set<ApiPredicateGroupItem> predicateItems;
    @NotNull


    public static ApiDefinitionVo assembleApiDefinition(ApiRule apiRule) {
        ApiDefinitionVo apiDefinitionVo = new ApiDefinitionVo();
        BeanUtils.copyProperties(apiRule, apiDefinitionVo);
        Set<ApiPredicateGroupItem> predicateItems = JSON.parseObject(apiRule.getApiPredicateItem(), new TypeReference<Set<ApiPredicateGroupItem>>() {
        });
        apiDefinitionVo.setPredicateItems(predicateItems);
        return apiDefinitionVo;
    }




}
