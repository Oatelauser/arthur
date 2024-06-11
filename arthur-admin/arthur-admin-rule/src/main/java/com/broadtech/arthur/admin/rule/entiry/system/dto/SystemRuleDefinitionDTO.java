package com.broadtech.arthur.admin.rule.entiry.system.dto;

import com.broadtech.arthur.admin.rule.entiry.system.nc.NcSystemRule;
import com.broadtech.arthur.admin.rule.entiry.system.po.SystemRule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/8/15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemRuleDefinitionDTO {
    private String id;
    private String groupId;
    private Double highestSystemLoad = -1.0;
    private Double highestCpuUsage = -1.0;
    private Double qps = -1.0;
    private Long avgRt = -1L;
    private Long maxThread = -1L;
    private String resource;
    private String limitApp;

    public SystemRule dto2po() {
        SystemRule systemRule = new SystemRule();
        BeanUtils.copyProperties(this,systemRule);
        return systemRule;
    }

    public NcSystemRule dto2nc() {
        NcSystemRule ncSystemRule = new NcSystemRule();
        BeanUtils.copyProperties(this,ncSystemRule);
        return ncSystemRule;
    }

}
