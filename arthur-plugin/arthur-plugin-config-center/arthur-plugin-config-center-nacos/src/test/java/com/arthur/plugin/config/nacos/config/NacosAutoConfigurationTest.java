package com.arthur.plugin.config.nacos.config;


import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.AbstractListener;
import com.alibaba.nacos.api.exception.NacosException;
import com.arthur.common.config.ConfigTemplate;
import com.arthur.plugin.config.nacos.NacosConfigManager;
import com.arthur.plugin.config.nacos.autoconfigure.NacosAutoConfiguration;
import com.arthur.plugin.config.nacos.autoconfigure.NacosProperties;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NacosAutoConfigurationTest {

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

    //@Test
    public void t1() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        String serverAddr = "192.168.5.153";
        String dataId = "arthur-dynamic-route.json";
        String group = "META_GROUP";

        String content = configService.getConfigAndSignListener(dataId, group, 30000, new AbstractListener() {
            @Override
            public void receiveConfigInfo(String configData) {
                System.out.println("MetaData：" + configData);
                try {
                    configService.getConfigAndSignListener("a", "DEFAULT_GROUP", 30000, new AbstractListener() {
                        @Override
                        public void receiveConfigInfo(String configInfo) {
                            System.out.println("a：" + configInfo);
                        }
                    });
                } catch (NacosException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public Executor getExecutor() {
                return executorService;
            }
        });

        Thread.sleep(1000 * 3600);
    }

    @Test
    public void t2() throws Exception {
        configService.publishConfig("a1", "b1", "1313131");
    }

    @Test
    public void configService() throws Exception {
        NacosAutoConfiguration configuration = new NacosAutoConfiguration();
        NacosProperties properties = new NacosProperties();
        properties.setUrl("192.168.5.153");
        //properties.setNamespace("public");
        properties.setUsername("nacos");
        properties.setPassword("nacos");

        //String serverAddr = "192.168.5.153";
        //Properties properties = new Properties();
        //properties.put(PropertyKeyConst.SERVER_ADDR, serverAddr);
        //properties.put(PropertyKeyConst.USERNAME, "nacos");
        //properties.put(PropertyKeyConst.PASSWORD, "nacos");
        //ConfigService configService = NacosFactory.createConfigService(properties);

        //service.publishConfig("a2", "b1", "1313131");

        ConfigTemplate configTemplate = configuration.configTemplate(properties, new NacosConfigManager());;
        configTemplate.pushConfig("b1", "a2", "1313131");

        configService.shutDown();
    }

}
