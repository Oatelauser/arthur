package com.broadtech.arthur.admin.rule.repository.flow.impl;

import com.broadtech.arthur.admin.common.service.impl.ServiceImpl;
import com.broadtech.arthur.admin.rule.entiry.flow.nc.NcFlowRule;
import com.broadtech.arthur.admin.rule.mapper.flow.NcFlowRuleMapper;
import com.broadtech.arthur.common.config.ConfigMetaData;
import com.broadtech.arthur.common.config.ConfigTemplate;
import org.springframework.stereotype.Service;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/8/17
 */
@Service
public class NcFlowRuleServiceImpl extends
        ServiceImpl<NcFlowRuleMapper, ConfigTemplate, ConfigMetaData,String, NcFlowRule> {
}
