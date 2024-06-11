package com.broadtech.arthur.admin.rule.entiry.degrade.dto;

import com.broadtech.arthur.admin.rule.entiry.degrade.nc.NcDegradeRule;
import com.broadtech.arthur.admin.rule.entiry.degrade.po.DegradeRule;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/8/15
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class DegradeRuleDefinitionDTO {
    private String id;
    private String groupId;
    private Integer grade = 0;
    private Double count;
    private Integer timeWindow;
    private Integer minRequestAmount = 5;
    private Double slowRatioThreshold = 1.0;
    private Integer statIntervalMs = 1000;
    private String resource;
    private String limitApp;


    public DegradeRule dto2po(){
        DegradeRule degradeRule = new DegradeRule();
        BeanUtils.copyProperties(this,degradeRule);
        return degradeRule;
    }

    public NcDegradeRule dto2nc(){
        NcDegradeRule ncDegradeRule = new NcDegradeRule();
        BeanUtils.copyProperties(this,ncDegradeRule);
        return ncDegradeRule;
    }


}
