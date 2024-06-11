package com.arthur.plugin.config.nacos;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.arthur.common.config.ConfigMetaData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Properties;

public class NacosConfigTemplateTest {

    private ConfigService configService;

    @BeforeEach
    public void setup() throws Exception {
        String serverAddr = "192.168.5.153";
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, serverAddr);
        properties.put(PropertyKeyConst.USERNAME, "nacos");
        properties.put(PropertyKeyConst.PASSWORD, "nacos");


        configService = NacosFactory.createConfigService(properties);
    }

    @AfterEach
    public void teardown() throws Exception {
        configService.shutDown();
    }

    @Test
    public void t1() throws Exception {
        NacosConfigTemplate configTemplate = new NacosConfigTemplate(null, configService);
        ConfigMetaData configMetaData = new ConfigMetaData("public", "b1", "a1","json");
        configTemplate.pushConfig(configMetaData, "12131");
    }

}
