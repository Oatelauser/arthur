package com.broadtech.arthur.admin.register.infrastructure.config;

import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.client.naming.NacosNamingService;
import org.springframework.context.annotation.Configuration;

/**
 * @author Machenike
 * date 2022/10/19
 * @version 1.0.0
 */
@Configuration
public class NacosConfig {


    public NamingService getNacosNamingService(NacosConfigProperties properties) throws NacosException {
       return NacosFactory.createNamingService(properties.assembleConfigServiceProperties());
    }

}
