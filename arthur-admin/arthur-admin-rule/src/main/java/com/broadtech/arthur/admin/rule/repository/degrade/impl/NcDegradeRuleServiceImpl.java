package com.broadtech.arthur.admin.rule.repository.degrade.impl;

import com.broadtech.arthur.admin.common.service.impl.ServiceImpl;
import com.broadtech.arthur.admin.rule.entiry.degrade.nc.NcDegradeRule;
import com.broadtech.arthur.admin.rule.mapper.degrade.NcDegradeRuleMapper;
import com.broadtech.arthur.common.config.ConfigMetaData;
import com.broadtech.arthur.common.config.ConfigTemplate;
import org.springframework.stereotype.Service;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/8/17
 */
@Service
public class NcDegradeRuleServiceImpl extends
        ServiceImpl<NcDegradeRuleMapper, ConfigTemplate, ConfigMetaData,String, NcDegradeRule> {
}
