package com.arthur.plugin.route.subscribe;

import com.arthur.common.config.ConfigMetaData;
import com.arthur.common.utils.JsonUtils;
import org.junit.jupiter.api.Test;

import java.util.Set;

class RouteMultiConfigListenerTest {

    @Test
    public void t1() {
        Set<ConfigMetaData> metaData = JsonUtils.toSet(null, ConfigMetaData.class);
        System.out.println(metaData);
    }

}
