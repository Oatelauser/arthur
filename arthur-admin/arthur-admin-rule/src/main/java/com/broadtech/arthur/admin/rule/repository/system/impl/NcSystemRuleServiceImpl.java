package com.broadtech.arthur.admin.rule.repository.system.impl;


import com.broadtech.arthur.admin.common.service.impl.ServiceImpl;
import com.broadtech.arthur.admin.rule.entiry.system.nc.NcSystemRule;
import com.broadtech.arthur.admin.rule.mapper.system.NcSystemRuleMapper;
import com.broadtech.arthur.common.config.ConfigMetaData;
import com.broadtech.arthur.common.config.ConfigTemplate;
import org.springframework.stereotype.Service;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/8/17
 */
@Service
public class NcSystemRuleServiceImpl extends
        ServiceImpl<NcSystemRuleMapper, ConfigTemplate, ConfigMetaData,String, NcSystemRule> {
}
