package com.arthur.nacos.client.naming;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.arthur.NacosPropertyHolder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Properties;

/**
 * 概要描述
 * <p>
 * 详细描述
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-03-02
 * @since 1.0
 */
class NacosNamingServiceAdapterTest {

    @Test
    void namingService() throws NacosException {
        Properties properties = NacosPropertyHolder.get();
        NacosNamingServiceAdapter namingService = new NacosNamingServiceAdapter(properties);
        List<Instance> allInstances = namingService.getAllInstances("arthur-user", false);
        Assertions.assertFalse(allInstances.isEmpty());
    }

}
