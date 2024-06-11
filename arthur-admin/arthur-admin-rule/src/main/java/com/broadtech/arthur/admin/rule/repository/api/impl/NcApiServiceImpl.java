package com.broadtech.arthur.admin.rule.repository.api.impl;

import com.broadtech.arthur.admin.common.service.impl.ServiceImpl;

import com.broadtech.arthur.admin.rule.entiry.api.nc.NcApiRule;
import com.broadtech.arthur.admin.rule.mapper.api.NcApiMapper;
import com.broadtech.arthur.common.config.ConfigMetaData;
import com.broadtech.arthur.common.config.ConfigTemplate;
import org.springframework.stereotype.Service;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/8/17
 */
@Service
public class NcApiServiceImpl extends ServiceImpl<NcApiMapper, ConfigTemplate, ConfigMetaData,String, NcApiRule> {
}
