package com.broadtech.arthur.admin.route.repository.impl;

import com.broadtech.arthur.admin.common.service.impl.ServiceImpl;
import com.broadtech.arthur.admin.route.entity.nc.GatewayRouteDefinition;
import com.broadtech.arthur.admin.route.mapper.NcRouteMapper;
import com.broadtech.arthur.common.config.ConfigMetaData;
import com.broadtech.arthur.common.config.ConfigTemplate;
import org.springframework.stereotype.Service;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/8/17
 */
@Service
public class NcRouteServiceImpl extends
        ServiceImpl<NcRouteMapper, ConfigTemplate, ConfigMetaData,String, GatewayRouteDefinition> {
}
