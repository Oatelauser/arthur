package com.broadtech.arthur.admin.common.config;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.undertow.Undertow;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.actuate.autoconfigure.metrics.export.ConditionalOnEnabledMetricsExport;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

/**
 * monitor undertow web container
 *
 * @author DearYang
 * @date 2022-07-14
 * @since 1.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnEnabledMetricsExport("prometheus")
@ConditionalOnClass({Undertow.class, PrometheusMeterRegistry.class})
public class UndertowHttpQueueMetricConfiguration implements ApplicationRunner {

    /**
     * 只初始化一次
     */
    private volatile boolean initialized = false;
    private final ObjectProvider<PrometheusMeterRegistry> provider;

    public UndertowHttpQueueMetricConfiguration(ObjectProvider<PrometheusMeterRegistry> provider) {
        this.provider = provider;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (initialized) {
            return;
        }

        PrometheusMeterRegistry registry = provider.getIfAvailable();
        if (registry == null) {
            return;
        }

        Gauge.builder("http_servlet_queue_size", () -> {
            try {
                return (Integer) ManagementFactory.getPlatformMBeanServer().getAttribute(new ObjectName("org.xnio:provider=\"nio\",type=Xnio,worker=\"XNIO-2\""), "WorkerPoolSize");
            } catch (Exception ignored) {
            }
            return -1;
        }).register(registry);
        initialized = true;
    }

}
