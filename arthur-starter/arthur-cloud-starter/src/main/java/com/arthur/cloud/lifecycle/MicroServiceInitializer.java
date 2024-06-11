package com.arthur.cloud.lifecycle;

import com.alibaba.nacos.client.constant.Constants;
import com.arthur.boot.lifecycle.AbstractServiceInitializer;
import com.arthur.boot.lifecycle.ServiceInitializer;
import com.arthur.common.lifecycle.InitializeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;

import java.util.List;

/**
 * 微服务初始化器
 *
 * @author DearYang
 * @date 2022-08-04
 * @see InitializeListener
 * @see ServiceInitializer
 * @since 1.0
 */
public class MicroServiceInitializer extends AbstractServiceInitializer {

    private static final Logger LOG = LoggerFactory.getLogger(MicroServiceInitializer.class);
    private final Registration registration;
    private final ServiceRegistry<Registration> serviceRegistry;

    public MicroServiceInitializer(ObjectProvider<List<InitializeListener>> provider,
			Registration registration, ServiceRegistry<Registration> serviceRegistry) {
        super(provider);
        this.registration = registration;
        this.serviceRegistry = serviceRegistry;
    }

    @Override
    protected void initialize() {
        serviceRegistry.setStatus(registration, Constants.HealthCheck.UP);
        LOG.info("Service Online");
    }

}
